package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.CustomerEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.VehicleEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VehicleReactive extends ReactiveSortingRepository<VehicleEntity, String> {
    Flux<VehicleEntity> findAllByCustomerAndLeaveDateIsNull(CustomerEntity customerEntity);

    Mono<VehicleEntity> findByReference(String reference);

    Mono<VehicleEntity> findByBin(String bin);

    @Query("{$and:["
            + "?#{ [0] == null ? {_id : {$ne:null}} : { bin : {$regex:[0], $options: 'i'} } },"
            + "?#{ [1] == null ? {_id : {$ne:null}} : { plate : {$regex:[1], $options: 'i'} } },"
            + "] }")
    Flux<VehicleEntity> findAllByBinAndPlateNullSafe(String bin, String plate);
}
