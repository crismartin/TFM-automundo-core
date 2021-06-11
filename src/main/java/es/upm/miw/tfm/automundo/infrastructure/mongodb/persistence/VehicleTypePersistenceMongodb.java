package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.exceptions.ConflictException;
import es.upm.miw.tfm.automundo.domain.exceptions.NotFoundException;
import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import es.upm.miw.tfm.automundo.domain.model.VehicleTypeCreation;
import es.upm.miw.tfm.automundo.domain.model.VehicleTypeUpdate;
import es.upm.miw.tfm.automundo.domain.persistence.VehicleTypePersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.VehicleTypeReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.VehicleTypeEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

    @Override
    public Mono<VehicleType> findByReference(String reference) {
        return this.vehicleTypeReactive.findByReference(reference)
                .switchIfEmpty(Mono.error(new NotFoundException("Non existent vehicle type with reference: " + reference)))
                .map(VehicleTypeEntity::toVehicleType);
    }

    @Override
    public Mono<VehicleType> create(VehicleTypeCreation vehicleTypeCreation) {
        return this.assertReferenceNotExist(vehicleTypeCreation.getReference())
                .then(Mono.just(new VehicleTypeEntity(vehicleTypeCreation)))
                .flatMap(this.vehicleTypeReactive::save)
                .map(VehicleTypeEntity::toVehicleType);
    }

    @Override
    public Mono<VehicleType> update(String reference, VehicleTypeUpdate vehicleTypeUpdate) {
        return this.vehicleTypeReactive.findByReference(reference)
                .switchIfEmpty(Mono.error(new NotFoundException("Cannot update. Non existent vehicle type " +
                        "with reference: " + reference)))
                .map(updatingVehicleType -> {
                    BeanUtils.copyProperties(vehicleTypeUpdate, updatingVehicleType);
                    return updatingVehicleType;
                }).flatMap(this.vehicleTypeReactive::save)
                .map(VehicleTypeEntity::toVehicleType);
    }

    private Mono<Void> assertReferenceNotExist(String reference) {
        return this.vehicleTypeReactive.findByReference(reference)
                .flatMap(vehicleTypeEntity -> Mono.error(
                        new ConflictException("Vehicle type reference already exists : " + reference)
                ));
    }
}
