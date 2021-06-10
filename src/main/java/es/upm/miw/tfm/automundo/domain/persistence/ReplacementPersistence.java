package es.upm.miw.tfm.automundo.domain.persistence;

import es.upm.miw.tfm.automundo.domain.model.Replacement;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ReplacementPersistence {
    Flux<Replacement> findByReferenceAndNameAndDescriptionNullSafe(
            String reference, String name, String description);
}
