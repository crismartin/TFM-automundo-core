package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.exceptions.NotFoundException;
import es.upm.miw.tfm.automundo.domain.model.Revision;
import es.upm.miw.tfm.automundo.domain.persistence.RevisionPersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.RevisionReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.TechnicianReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.VehicleReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.RevisionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public class RevisionPersistenceMongodb implements RevisionPersistence {

    private VehicleReactive vehicleReactive;
    private RevisionReactive revisionReactive;
    private TechnicianReactive technicianReactive;

    @Autowired
    public RevisionPersistenceMongodb(VehicleReactive vehicleReactive, RevisionReactive revisionReactive,
                                      TechnicianReactive technicianReactive){
        this.vehicleReactive = vehicleReactive;
        this.revisionReactive = revisionReactive;
        this.technicianReactive = technicianReactive;
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
}
