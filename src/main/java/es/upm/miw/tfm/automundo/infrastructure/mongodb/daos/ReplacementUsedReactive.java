package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.ReplacementUsedEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;

public interface ReplacementUsedReactive extends ReactiveSortingRepository<ReplacementUsedEntity, String> {
}
