package es.upm.miw.tfm.automundo.domain.persistence;

import es.upm.miw.tfm.automundo.domain.model.Revision;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface RevisionPersistence {
    Flux<Revision> findAllByVehicleReference(String reference);

    Mono<Revision> create(Revision revision);

    Mono<Revision> createReplacementsUsed(Revision revision);

    Mono<Revision> findByReference(String reference);

    Mono<Revision> update(Revision revision);

    Mono<Revision> updateCostByReference(String reference);

    Mono<Void> deleteLogic(String reference);
}
