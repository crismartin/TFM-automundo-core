package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.exceptions.NotFoundException;
import es.upm.miw.tfm.automundo.domain.model.User;
import es.upm.miw.tfm.automundo.domain.persistence.UserPersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.UserReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class UserPersistenceMongodb implements UserPersistence {

    private UserReactive userReactive;

    @Autowired
    public UserPersistenceMongodb(UserReactive userReactive) {
        this.userReactive = userReactive;
    }

    @Override
    public Mono<User> readByUserName(String userName) {
        return this.userReactive.findByUserName(userName)
                .switchIfEmpty(Mono.error(new NotFoundException("Non existent user: " + userName)))
                .map(UserEntity::toUser);
    }
}
