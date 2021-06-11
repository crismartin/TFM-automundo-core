package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.exceptions.ConflictException;
import es.upm.miw.tfm.automundo.domain.exceptions.NotFoundException;
import es.upm.miw.tfm.automundo.domain.model.Replacement;
import es.upm.miw.tfm.automundo.domain.model.ReplacementCreation;
import es.upm.miw.tfm.automundo.domain.model.ReplacementUpdate;
import es.upm.miw.tfm.automundo.domain.persistence.ReplacementPersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.ReplacementReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.ReplacementEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class ReplacementPersistenceMongodb implements ReplacementPersistence {

    private ReplacementReactive replacementReactive;

    @Autowired
    public ReplacementPersistenceMongodb(ReplacementReactive replacementReactive) {
        this.replacementReactive = replacementReactive;
    }

    @Override
    public Flux<Replacement> findByReferenceAndNameAndDescriptionNullSafe(String reference, String name, String description) {
        return this.replacementReactive.findByReferenceAndNameAndDescriptionNullSafe(reference, name, description)
                .map(ReplacementEntity::toReplacement);
    }

    @Override
    public Mono<Replacement> findByReference(String reference) {
        return this.replacementReactive.findByReference(reference)
                .switchIfEmpty(Mono.error(new NotFoundException("Non existent replacement with reference: " + reference)))
                .map(ReplacementEntity::toReplacement);
    }

    @Override
    public Mono<Replacement> create(ReplacementCreation replacementCreation) {
        return this.assertReferenceNotExist(replacementCreation.getReference())
                .then(Mono.just(new ReplacementEntity(replacementCreation)))
                .flatMap(this.replacementReactive::save)
                .map(ReplacementEntity::toReplacement);
    }

    @Override
    public Mono<Replacement> update(String reference, ReplacementUpdate replacementUpdate) {
        return this.replacementReactive.findByReference(reference)
                .switchIfEmpty(Mono.error(new NotFoundException("Cannot update. Non existent replacement " +
                        "with reference: " + reference)))
                .map(updatingReplacement -> {
                    BeanUtils.copyProperties(replacementUpdate, updatingReplacement);
                    return updatingReplacement;
                }).flatMap(this.replacementReactive::save)
                .map(ReplacementEntity::toReplacement);
    }

    private Mono<Void> assertReferenceNotExist(String reference) {
        return this.replacementReactive.findByReference(reference)
                .flatMap(replacementEntity -> Mono.error(
                        new ConflictException("Replacement reference already exists : " + reference)
                ));
    }
}
