package es.upm.miw.tfm.automundo.domain.persistence;

import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import es.upm.miw.tfm.automundo.domain.model.VehicleTypeCreation;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface VehicleTypePersistence {
    Flux<VehicleType> findByReferenceAndNameAndDescriptionNullSafe(
            String reference, String name, String description);

    Mono<VehicleType> findByReference(String reference);

    Mono<VehicleType> create(VehicleTypeCreation vehicleTypeCreation);
}
