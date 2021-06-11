package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.VehicleTypeEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VehicleTypeReactive extends ReactiveSortingRepository<VehicleTypeEntity, String> {

    @Query("{$and:["
            + "?#{ [0] == null ? {_id : {$ne:null}} : { reference : {$regex:[0], $options: 'i'} } },"
            + "?#{ [1] == null ? {_id : {$ne:null}} : { name : {$regex:[1], $options: 'i'} } },"
            + "?#{ [2] == null ? {_id : {$ne:null}} : { description : {$regex:[2], $options: 'i'} } },"
            + "] }")
    Flux<VehicleTypeEntity> findByReferenceAndNameAndDescriptionNullSafe(String reference, String name, String description);

    Mono<VehicleTypeEntity> findByReference(String reference);
}
