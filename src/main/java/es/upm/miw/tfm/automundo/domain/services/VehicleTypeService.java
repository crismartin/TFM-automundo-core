package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import es.upm.miw.tfm.automundo.domain.persistence.VehicleTypePersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class VehicleTypeService {
    private VehicleTypePersistence vehicleTypePersistence;

    @Autowired
    public VehicleTypeService(VehicleTypePersistence vehicleTypePersistence) {
        this.vehicleTypePersistence = vehicleTypePersistence;
    }

    public Flux<VehicleType> findByReferenceAndNameAndDescriptionNullSafe(
            String reference, String name, String description) {
        return this.vehicleTypePersistence.findByReferenceAndNameAndDescriptionNullSafe(
                reference, name, description);
    }
}
