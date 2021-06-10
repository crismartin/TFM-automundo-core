package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.model.Replacement;
import es.upm.miw.tfm.automundo.domain.persistence.ReplacementPersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.ReplacementReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.ReplacementEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

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
}
