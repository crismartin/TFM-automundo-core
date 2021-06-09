package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.VehicleEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VehicleDao extends MongoRepository<VehicleEntity, String> {

}
