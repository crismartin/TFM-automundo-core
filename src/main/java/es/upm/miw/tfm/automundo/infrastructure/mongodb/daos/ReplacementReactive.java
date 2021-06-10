package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.domain.model.Replacement;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.ReplacementEntity;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Flux;

public interface ReplacementReactive extends ReactiveSortingRepository<ReplacementEntity, String> {

    @Query("{$and:["
            + "?#{ [0] == null ? {_id : {$ne:null}} : { reference : {$regex:[0], $options: 'i'} } },"
            + "?#{ [1] == null ? {_id : {$ne:null}} : { name : {$regex:[1], $options: 'i'} } },"
            + "?#{ [2] == null ? {_id : {$ne:null}} : { description : {$regex:[2], $options: 'i'} } },"
            + "] }")
    Flux<ReplacementEntity> findByReferenceAndNameAndDescriptionNullSafe(String reference, String name, String description);
}
