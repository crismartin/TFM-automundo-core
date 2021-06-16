package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.synchronous;

import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.CustomerEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface CustomerDao extends MongoRepository<CustomerEntity, String> {
    List<CustomerEntity> findByIdentificationId(String identificationId);
}
