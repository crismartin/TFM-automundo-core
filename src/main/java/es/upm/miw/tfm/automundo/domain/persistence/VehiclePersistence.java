package es.upm.miw.tfm.automundo.domain.persistence;

import es.upm.miw.tfm.automundo.domain.model.Vehicle;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VehiclePersistence {
    Flux<Vehicle> findVehiclesByIdCustomer(String identificationId);

    Mono<Vehicle> findByReference(String reference);

    Mono<Vehicle> create(Vehicle vehicle);

    Mono<Vehicle> update(Vehicle vehicle);

    Mono<Void> deleteLogic(String reference);

    Mono<Void> deleteByCustomerIdentificationId(String customerIdentificationId);
}
