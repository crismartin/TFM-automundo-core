package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import es.upm.miw.tfm.automundo.domain.model.VehicleTypeCreation;
import es.upm.miw.tfm.automundo.domain.model.VehicleTypeUpdate;
import es.upm.miw.tfm.automundo.domain.persistence.VehicleTypePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class VehicleTypeService {
    private VehicleTypePersistence vehicleTypePersistence;

    @Autowired
    public VehicleTypeService(VehicleTypePersistence vehicleTypePersistence) {
        this.vehicleTypePersistence = vehicleTypePersistence;
    }

    public Flux<VehicleType> findByReferenceAndNameAndDescriptionAndActiveNullSafe(
            String reference, String name, String description, Boolean active) {
        return this.vehicleTypePersistence.findByReferenceAndNameAndDescriptionAndActiveNullSafe(
                reference, name, description, active);
    }

    public Mono<VehicleType> read(String reference) {
        return this.vehicleTypePersistence.findByReference(reference);
    }

    public Mono<VehicleType> create(VehicleTypeCreation vehicleTypeCreation) {
        return this.vehicleTypePersistence.create(vehicleTypeCreation);
    }

    public Mono<VehicleType> update(String reference, VehicleTypeUpdate vehicleTypeUpdate) {
        return this.vehicleTypePersistence.update(reference, vehicleTypeUpdate);
    }
}
