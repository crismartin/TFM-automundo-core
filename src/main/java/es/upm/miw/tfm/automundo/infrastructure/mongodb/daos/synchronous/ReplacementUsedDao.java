package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.ReplacementUsedEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReplacementUsedDao extends MongoRepository<ReplacementUsedEntity, String> {
}
