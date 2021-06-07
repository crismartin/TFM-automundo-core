package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.persistence.CustomerPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class CustomerService {

    private CustomerPersistence customerPersistence;

    @Autowired
    public CustomerService(CustomerPersistence customerPersistence) {
        this.customerPersistence = customerPersistence;
    }

    public Flux<Customer> findByIdentificationidAndNameAndSurnameAndSecondsurnameNullSafe(
            String identificationId, String name, String surName, String secondSurName) {
        return this.customerPersistence.findByIdentificationidAndNameAndSurnameAndSecondsurnameNullSafe(
                identificationId, name, surName, secondSurName);
    }
}
