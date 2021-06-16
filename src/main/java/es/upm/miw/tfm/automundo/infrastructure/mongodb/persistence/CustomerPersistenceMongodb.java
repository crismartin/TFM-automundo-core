package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.exceptions.ConflictException;
import es.upm.miw.tfm.automundo.domain.exceptions.NotFoundException;
import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.model.CustomerCreation;
import es.upm.miw.tfm.automundo.domain.model.CustomerUpdate;
import es.upm.miw.tfm.automundo.domain.persistence.CustomerPersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.CustomerReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.CustomerEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class CustomerPersistenceMongodb implements CustomerPersistence {

    private CustomerReactive customerReactive;

    @Autowired
    public CustomerPersistenceMongodb(CustomerReactive customerReactive) {
        this.customerReactive = customerReactive;
    }

    @Override
    public Flux<Customer> findByIdentificationIdAndNameAndSurNameAndSecondSurNameNullSafe(String identificationId, String name, String surName, String secondSurName) {
        return this.customerReactive.findByIdentificationidAndNameAndSurnameAndSecondsurnameNullSafe(identificationId, name, surName, secondSurName)
                .map(CustomerEntity::toCustomer);
    }

    @Override
    public Mono<Customer> findByIdentificationId(String identification) {
        return this.customerReactive.findByIdentificationId(identification)
                .switchIfEmpty(Mono.error(new NotFoundException("Non existent customer with identification id: " + identification)))
                .map(CustomerEntity::toCustomer);
    }

    @Override
    public Mono<Customer> create(CustomerCreation customerCreation) {
        return this.assertIdentificationIdNotExist(customerCreation.getIdentificationId())
                .then(Mono.just(new CustomerEntity(customerCreation)))
                .flatMap(this.customerReactive::save)
                .map(CustomerEntity::toCustomer);
    }

    @Override
    public Mono<Customer> update(String identification, CustomerUpdate customerUpdate) {
        return this.customerReactive.findByIdentificationId(identification)
                .switchIfEmpty(Mono.error(new NotFoundException("Cannot update. Non existent customer " +
                        "with identification id: " + identification)))
                .map(updatingCustomer -> {
                    BeanUtils.copyProperties(customerUpdate, updatingCustomer);
                    return updatingCustomer;
                }).flatMap(this.customerReactive::save)
                .map(CustomerEntity::toCustomer);
    }

    @Override
    public Mono<Void> delete(String identification) {
        //TODO before find vehicles with Customer.IdentificationId and delete it
        return this.customerReactive.findByIdentificationId(identification)
                .switchIfEmpty(Mono.error(new NotFoundException("Cannot delete. Non existent customer " +
                        "with identification id: " + identification)))
                .then(this.customerReactive.deleteByIdentificationId(identification));
    }

    private Mono<Void> assertIdentificationIdNotExist(String identificationId) {
        return this.customerReactive.findByIdentificationId(identificationId)
                .flatMap(customerEntity -> Mono.error(
                        new ConflictException("Customer identification id already exists : " + identificationId)
                ));
    }
}
