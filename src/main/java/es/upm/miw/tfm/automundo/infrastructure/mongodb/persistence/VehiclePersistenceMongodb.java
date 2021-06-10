package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.exceptions.NotFoundException;
import es.upm.miw.tfm.automundo.domain.model.Vehicle;
import es.upm.miw.tfm.automundo.domain.persistence.VehiclePersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.CustomerReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.VehicleReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.VehicleEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class VehiclePersistenceMongodb implements VehiclePersistence {

    private VehicleReactive vehicleReactive;

    private CustomerReactive customerReactive;

    @Autowired
    public VehiclePersistenceMongodb(VehicleReactive vehicleReactive, CustomerReactive customerReactive){
        this.vehicleReactive = vehicleReactive;
        this.customerReactive = customerReactive;
    }

    @Override
    public Flux<Vehicle> findVehiclesByIdCustomer(String identificationId) {
        return customerReactive.findByIdentificationId(identificationId)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer Identification: " + identificationId)))
                .flatMapMany(customerEntity -> this.vehicleReactive.findAllByCustomer(customerEntity)
                .map(VehicleEntity::toVehicle));
    }

    @Override
    public Mono<Vehicle> findByReference(String reference) {
        return vehicleReactive.findByReference(reference)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Reference: " + reference)))
                .map(VehicleEntity::toVehicle);
    }
}
