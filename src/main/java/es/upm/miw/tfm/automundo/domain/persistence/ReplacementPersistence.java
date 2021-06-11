package es.upm.miw.tfm.automundo.domain.persistence;

import es.upm.miw.tfm.automundo.domain.model.Replacement;
import es.upm.miw.tfm.automundo.domain.model.ReplacementCreation;
import es.upm.miw.tfm.automundo.domain.model.ReplacementUpdate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReplacementPersistence {
    Flux<Replacement> findByReferenceAndNameAndDescriptionNullSafe(
            String reference, String name, String description);

    Mono<Replacement> findByReference(String reference);

    Mono<Replacement> create(ReplacementCreation replacementCreation);

    Mono<Replacement> update(String reference, ReplacementUpdate replacementUpdate);
}
