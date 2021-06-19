package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.ReplacementUsedEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.RevisionEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface ReplacementUsedReactive extends ReactiveSortingRepository<ReplacementUsedEntity, String> {
    Flux<ReplacementUsedEntity> findAllByRevisionEntity(RevisionEntity revisionEntity);

}
