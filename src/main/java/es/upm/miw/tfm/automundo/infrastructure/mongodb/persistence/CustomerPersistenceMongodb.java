package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.persistence.CustomerPersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.CustomerReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class CustomerPersistenceMongodb implements CustomerPersistence {

    private CustomerReactive customerReactive;

    @Autowired
    public CustomerPersistenceMongodb(CustomerReactive customerReactive) {
        this.customerReactive = customerReactive;
    }

    @Override
    public Flux<Customer> findByIdentificationidAndNameAndSurnameAndSecondsurnameNullSafe(String identificationId, String name, String surName, String secondSurName) {
        return this.customerReactive.findByIdentificationidAndNameAndSurnameAndSecondsurnameNullSafe(identificationId, name, surName, secondSurName)
                .map(CustomerEntity::toCustomer);
    }
}
