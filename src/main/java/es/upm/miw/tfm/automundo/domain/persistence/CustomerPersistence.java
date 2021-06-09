package es.upm.miw.tfm.automundo.domain.persistence;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.model.CustomerCreation;
import es.upm.miw.tfm.automundo.domain.model.CustomerUpdate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface CustomerPersistence {

    Flux<Customer> findByIdentificationIdAndNameAndSurNameAndSecondSurNameNullSafe(
            String identificationId, String name, String surName, String secondSurName);

    Mono<Customer> findByIdentificationId(String identification);

    Mono<Customer> create(CustomerCreation customerCreation);

    Mono<Customer> update(String identification, CustomerUpdate customerUpdate);
}
