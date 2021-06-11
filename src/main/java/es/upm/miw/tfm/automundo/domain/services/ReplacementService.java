package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.domain.model.Replacement;
import es.upm.miw.tfm.automundo.domain.model.ReplacementCreation;
import es.upm.miw.tfm.automundo.domain.model.ReplacementUpdate;
import es.upm.miw.tfm.automundo.domain.persistence.ReplacementPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReplacementService {

    private ReplacementPersistence replacementPersistence;

    @Autowired
    public ReplacementService(ReplacementPersistence replacementPersistence) {
        this.replacementPersistence = replacementPersistence;
    }

    public Flux<Replacement> findByReferenceAndNameAndDescriptionNullSafe(
            String reference, String name, String description) {
        return this.replacementPersistence.findByReferenceAndNameAndDescriptionNullSafe(
                reference, name, description);
    }

    public Mono<Replacement> read(String reference) {
        return this.replacementPersistence.findByReference(reference);
    }

    public Mono<Replacement> create(ReplacementCreation replacementCreation) {
        return this.replacementPersistence.create(replacementCreation);
    }

    public Mono<Replacement> update(String reference, ReplacementUpdate replacementUpdate) {
        return this.replacementPersistence.update(reference, replacementUpdate);
    }
}
