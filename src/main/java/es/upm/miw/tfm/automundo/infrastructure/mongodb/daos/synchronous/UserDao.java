package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.Role;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Collection;
import java.util.List;

public interface UserDao extends MongoRepository<UserEntity, String> {
    List<UserEntity> findByRoleIn(Collection<Role> roles);
}
