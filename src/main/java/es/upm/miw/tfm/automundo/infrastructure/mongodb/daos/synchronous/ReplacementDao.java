package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.ReplacementEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReplacementDao extends MongoRepository<ReplacementEntity, String> {
}
