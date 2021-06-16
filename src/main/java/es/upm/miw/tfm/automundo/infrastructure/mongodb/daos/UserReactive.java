package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.UserEntity;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import reactor.core.publisher.Mono;

public interface UserReactive extends ReactiveSortingRepository<UserEntity, String> {
    Mono<UserEntity> findByUserName(String userName);
}
