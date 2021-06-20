package es.upm.miw.tfm.automundo.domain.persistence;

import es.upm.miw.tfm.automundo.domain.model.ReplacementUsed;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface ReplacementUsedPersistence {
    Mono<ReplacementUsed> update(ReplacementUsed replacementUsed);

    Mono<ReplacementUsed> create(ReplacementUsed replacementUsed);

    Flux<ReplacementUsed> findAllByRevisionReference(String revisionReference);

    Mono<String> delete(String reference);
}
