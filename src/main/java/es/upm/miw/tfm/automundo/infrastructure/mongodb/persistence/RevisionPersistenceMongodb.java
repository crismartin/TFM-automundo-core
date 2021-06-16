package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.exceptions.NotFoundException;
import es.upm.miw.tfm.automundo.domain.model.Revision;
import es.upm.miw.tfm.automundo.domain.persistence.RevisionPersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.*;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.ReplacementUsedEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.RevisionEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.stream.Collectors;


@Repository
public class RevisionPersistenceMongodb implements RevisionPersistence {

    private VehicleReactive vehicleReactive;
    private RevisionReactive revisionReactive;
    private TechnicianReactive technicianReactive;
    private ReplacementUsedReactive replacementUsedReactive;
    private ReplacementReactive replacementReactive;

    @Autowired
    public RevisionPersistenceMongodb(VehicleReactive vehicleReactive, RevisionReactive revisionReactive,
                                      TechnicianReactive technicianReactive, ReplacementUsedReactive replacementUsedReactive,
                                      ReplacementReactive replacementReactive){
        this.vehicleReactive = vehicleReactive;
        this.revisionReactive = revisionReactive;
        this.technicianReactive = technicianReactive;
        this.replacementUsedReactive = replacementUsedReactive;
        this.replacementReactive = replacementReactive;
    }

    @Override
    public Flux<Revision> findAllByVehicleReference(String reference) {
        return vehicleReactive.findByReference(reference)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Reference: " + reference)))
                .flatMapMany(vehicleEntity -> this.revisionReactive.findAllByVehicleEntity(vehicleEntity)
                        .map(RevisionEntity::toRevision)
                );
    }

    @Override
    public Mono<Revision> create(Revision revision) {
        RevisionEntity revisionEntity = new RevisionEntity(revision);
        return vehicleReactive.findByReference(revision.getVehicleReference())
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Reference: " + revision.getVehicleReference())))
                .flatMap(vehicleEntity -> {
                    revisionEntity.setFieldsCreation();
                    revisionEntity.setVehicleEntity(vehicleEntity);
                    return this.technicianReactive.findByIdentificationId(revision.getTechnicianIdentification())
                            .switchIfEmpty( Mono.error(new NotFoundException("Technician Identification: " + revision.getTechnicianIdentification())) )
                            .map(technicianEntity -> {
                                revisionEntity.setTechnicianEntity(technicianEntity);
                                return revisionEntity;
                            });
                })
                .flatMap(this.revisionReactive::save)
                .map(RevisionEntity::toRevision);
    }

    private Mono<RevisionEntity> findByReference(String reference){
        return revisionReactive.findByReference(reference)
                .switchIfEmpty(Mono.error(new NotFoundException("Revision Reference: " + reference)));
    }

    @Override
    public Mono<Revision> createReplacementsUsed(Revision revision) {

        return findByReference(revision.getReference())
                .flatMap(revisionEntity ->
                    Flux.fromStream(revision.getReplacementsUsed().stream())
                        .flatMap(replacementUsed -> {
                            ReplacementUsedEntity replacementUsedEntity = new ReplacementUsedEntity(replacementUsed);
                            replacementUsedEntity.setRevisionEntity(revisionEntity);
                            return this.replacementReactive.findByReference(replacementUsed.getReplacementReference())
                                    .switchIfEmpty(Mono.error(new NotFoundException("Replacement reference: " + replacementUsed.getReplacementReference())))
                                    .map(replacementEntity -> {
                                        replacementUsedEntity.setReplacementEntity(replacementEntity);
                                        return replacementUsedEntity;
                                    });
                        })
                        .flatMap(replacementUsedEntity -> {
                            replacementUsedEntity.setReference(UUID.randomUUID().toString());
                            return this.replacementUsedReactive.save(replacementUsedEntity);
                        })
                        .map(ReplacementUsedEntity::toReplacementUsed)
                        .collect(Collectors.toList())
                )
                .map(replacementUseds -> {
                    revision.setReplacementsUsed(replacementUseds);
                    return revision;
                });
    }
}
