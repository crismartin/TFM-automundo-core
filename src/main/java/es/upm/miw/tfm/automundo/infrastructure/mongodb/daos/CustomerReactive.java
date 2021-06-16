package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.CustomerEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomerReactive extends ReactiveSortingRepository<CustomerEntity, String> {

    @Query("{$and:["
            + "?#{ [0] == null ? {_id : {$ne:null}} : { identificationId : {$regex:[0], $options: 'i'} } },"
            + "?#{ [1] == null ? {_id : {$ne:null}} : { name : {$regex:[1], $options: 'i'} } },"
            + "?#{ [2] == null ? {_id : {$ne:null}} : { surName : {$regex:[2], $options: 'i'} } },"
            + "?#{ [3] == null ? {_id : {$ne:null}} : { secondSurName : {$regex:[3], $options: 'i'} } },"
            + "] }")
    Flux<CustomerEntity> findByIdentificationidAndNameAndSurnameAndSecondsurnameNullSafe(
            String identificationId, String name, String surName, String secondSurName);

    Mono<CustomerEntity> findByIdentificationId(String identification);

    Mono<Void> deleteByIdentificationId(String identification);
}
