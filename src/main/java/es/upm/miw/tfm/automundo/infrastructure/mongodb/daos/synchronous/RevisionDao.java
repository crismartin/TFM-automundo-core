package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.RevisionEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RevisionDao extends MongoRepository<RevisionEntity, String> {
}
