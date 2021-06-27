package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.model.CustomerCreation;
import es.upm.miw.tfm.automundo.domain.model.CustomerUpdate;
import es.upm.miw.tfm.automundo.domain.persistence.CustomerPersistence;
import es.upm.miw.tfm.automundo.domain.persistence.VehiclePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerService {

    private CustomerPersistence customerPersistence;
    private VehiclePersistence vehiclePersistence;

    @Autowired
    public CustomerService(CustomerPersistence customerPersistence, VehiclePersistence vehiclePersistence) {
        this.customerPersistence = customerPersistence;
        this.vehiclePersistence = vehiclePersistence;
    }

    public Flux<Customer> findByIdentificationIdAndNameAndSurNameAndSecondSurNameNullSafe(
            String identificationId, String name, String surName, String secondSurName) {
        return this.customerPersistence.findByIdentificationIdAndNameAndSurNameAndSecondSurNameNullSafe(
                identificationId, name, surName, secondSurName);
    }

    public Mono<Customer> read(String identification) {
        return this.customerPersistence.findByIdentificationId(identification);
    }

    public Mono<Customer> create(CustomerCreation customerCreation) {
        return this.customerPersistence.create(customerCreation);
    }

    public Mono<Customer> update(String identification, CustomerUpdate customerUpdate) {
        return this.customerPersistence.update(identification, customerUpdate);
    }

    public Mono<Void> delete(String identification) {
        return customerPersistence.deleteLogic(identification)
                .then(vehiclePersistence.findVehiclesByIdCustomer(identification)
                        .doOnNext(vehicle -> vehiclePersistence.deleteLogic(vehicle.getReference()))
                        .then()
                );
    }
}
