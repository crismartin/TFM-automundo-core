package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.OwnerTypeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface OwnerTypeDao extends MongoRepository<OwnerTypeEntity, String> {

}
