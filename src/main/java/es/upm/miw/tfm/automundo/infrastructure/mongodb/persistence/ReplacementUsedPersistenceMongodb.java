package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.exceptions.NotFoundException;
import es.upm.miw.tfm.automundo.domain.model.ReplacementUsed;
import es.upm.miw.tfm.automundo.domain.persistence.ReplacementUsedPersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.ReplacementReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.ReplacementUsedReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.RevisionReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.ReplacementEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.ReplacementUsedEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.RevisionEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public class ReplacementUsedPersistenceMongodb implements ReplacementUsedPersistence {

    private ReplacementUsedReactive replacementUsedReactive;
    private RevisionReactive revisionReactive;
    private ReplacementReactive replacementReactive;

    @Autowired
    public ReplacementUsedPersistenceMongodb(ReplacementUsedReactive replacementUsedReactive, RevisionReactive revisionReactive,
                                             ReplacementReactive replacementReactive){
        this.replacementUsedReactive = replacementUsedReactive;
        this.revisionReactive = revisionReactive;
        this.replacementReactive = replacementReactive;
    }

    private Mono<RevisionEntity> findRevisionEntityByReference(String reference) {
        return revisionReactive.findByReference(reference)
                .switchIfEmpty(Mono.error(new NotFoundException("Revision Reference: " + reference)));
    }

    private Mono<ReplacementEntity> findReplacementEntityByReference(String reference) {
        return replacementReactive.findByReference(reference)
                .switchIfEmpty(Mono.error(new NotFoundException("Replacement Reference: " + reference)));
    }

    @Override
    public Mono<ReplacementUsed> update(ReplacementUsed replacementUsed) {

        return replacementUsedReactive.findByReference(replacementUsed.getReference())
                .switchIfEmpty(Mono.error(new NotFoundException("Replacement Used Reference: " + replacementUsed.getReference())))
                .flatMap(replacementUsedEntityDb -> {
                    BeanUtils.copyProperties(replacementUsed, replacementUsedEntityDb);
                    return findRevisionEntityByReference(replacementUsed.getRevisionReference())
                            .flatMap(revisionEntity -> {
                                replacementUsedEntityDb.setRevisionEntity(revisionEntity);
                                return findReplacementEntityByReference(replacementUsed.getReplacementReference())
                                        .flatMap(replacementEntity -> {
                                            replacementUsedEntityDb.setReplacementEntity(replacementEntity);
                                            return this.replacementUsedReactive.save(replacementUsedEntityDb)
                                                    .map(ReplacementUsedEntity::toReplacementUsed);
                                        });
                            });
                });
    }

    @Override
    public Mono<ReplacementUsed> create(ReplacementUsed replacementUsed) {
        return findRevisionEntityByReference(replacementUsed.getRevisionReference())
                .flatMap(revisionEntity -> findReplacementEntityByReference(replacementUsed.getReplacementReference())
                    .flatMap(replacementEntity -> {
                        ReplacementUsedEntity replacementUsedEntity = new ReplacementUsedEntity(replacementUsed);
                        replacementUsedEntity.setRevisionEntity(revisionEntity);
                        replacementUsedEntity.setReplacementEntity(replacementEntity);
                        replacementUsedEntity.setReference(UUID.randomUUID().toString());
                        return this.replacementUsedReactive.save(replacementUsedEntity)
                                .map(ReplacementUsedEntity::toReplacementUsed);
                    })
                );
    }

    @Override
    public Flux<ReplacementUsed> findAllByRevisionReference(String revisionReference) {
        return findRevisionEntityByReference(revisionReference)
                .flatMapMany(revisionEntity -> replacementUsedReactive.findAllByRevisionEntity(revisionEntity)
                        .map(ReplacementUsedEntity::toReplacementUsed)
                );
    }
}
