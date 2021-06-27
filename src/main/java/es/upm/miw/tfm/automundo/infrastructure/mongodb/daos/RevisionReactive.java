package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.RevisionEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.VehicleEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface RevisionReactive extends ReactiveSortingRepository<RevisionEntity, String> {
    Flux<RevisionEntity> findAllByVehicleEntityAndLeaveDateIsNull(VehicleEntity vehicleEntity);

    Mono<RevisionEntity> findByReference(String reference);

    Mono<Void> deleteByVehicleEntity(VehicleEntity vehicleEntity);
}
