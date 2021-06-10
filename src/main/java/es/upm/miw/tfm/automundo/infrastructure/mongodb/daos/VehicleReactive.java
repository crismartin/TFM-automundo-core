package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.CustomerEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.VehicleEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VehicleReactive extends ReactiveSortingRepository<VehicleEntity, String> {
    Flux<VehicleEntity> findAllByCustomer(CustomerEntity customerEntity);

    Mono<VehicleEntity> findByReference(String reference);
}
