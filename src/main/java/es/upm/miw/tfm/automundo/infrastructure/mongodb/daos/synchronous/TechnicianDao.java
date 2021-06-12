package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.TechnicianEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TechnicianDao extends MongoRepository<TechnicianEntity, String> {
}
