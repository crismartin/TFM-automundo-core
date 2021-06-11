package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import es.upm.miw.tfm.automundo.domain.persistence.VehicleTypePersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.VehicleTypeReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.VehicleTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class VehicleTypePersistenceMongodb implements VehicleTypePersistence {
    private VehicleTypeReactive vehicleTypeReactive;

    @Autowired
    public VehicleTypePersistenceMongodb(VehicleTypeReactive vehicleTypeReactive) {
        this.vehicleTypeReactive = vehicleTypeReactive;
    }

    @Override
    public Flux<VehicleType> findByReferenceAndNameAndDescriptionNullSafe(String reference, String name, String description) {
        return this.vehicleTypeReactive.findByReferenceAndNameAndDescriptionNullSafe(reference, name, description)
                .map(VehicleTypeEntity::toVehicleType);
    }
}
