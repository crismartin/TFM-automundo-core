package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.VehicleTypeEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VehicleTypeDao extends MongoRepository<VehicleTypeEntity, String> {
}
