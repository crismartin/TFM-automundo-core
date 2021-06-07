package es.upm.miw.tfm.automundo.domain.persistence;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerPersistence {

    Flux<Customer> findByIdentificationidAndNameAndSurnameAndSecondsurnameNullSafe(
            String identificationId, String name, String surName, String secondSurName);
}
