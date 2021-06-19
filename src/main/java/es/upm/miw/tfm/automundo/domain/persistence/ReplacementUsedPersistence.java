package es.upm.miw.tfm.automundo.domain.persistence;

import es.upm.miw.tfm.automundo.domain.model.ReplacementUsed;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ReplacementUsedPersistence {
    Mono<ReplacementUsed> update(ReplacementUsed replacementUsed);
}
