package es.upm.miw.tfm.automundo.domain.persistence;

import es.upm.miw.tfm.automundo.domain.model.Vehicle;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VehiclePersistence {
    Flux<Vehicle> findVehiclesByIdCustomer(String identificationId);

    Mono<Vehicle> findByReference(String reference);
}
