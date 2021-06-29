package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.exceptions.ConflictException;
import es.upm.miw.tfm.automundo.domain.exceptions.NotFoundException;
import es.upm.miw.tfm.automundo.domain.model.Vehicle;
import es.upm.miw.tfm.automundo.domain.persistence.VehiclePersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.CustomerReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.VehicleReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.VehicleTypeReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.CustomerEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.VehicleEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.VehicleTypeEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;


@Repository
public class VehiclePersistenceMongodb implements VehiclePersistence {

    private VehicleReactive vehicleReactive;
    private VehicleTypeReactive vehicleTypeReactive;
    private CustomerReactive customerReactive;


    @Autowired
    public VehiclePersistenceMongodb(VehicleReactive vehicleReactive, VehicleTypeReactive vehicleTypeReactive,
                                     CustomerReactive customerReactive){
        this.vehicleReactive = vehicleReactive;
        this.vehicleTypeReactive = vehicleTypeReactive;
        this.customerReactive = customerReactive;
    }

    @Override
    public Flux<Vehicle> findVehiclesByIdCustomer(String identificationId) {
        return customerReactive.findByIdentificationId(identificationId)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer Identification: " + identificationId)))
                .flatMapMany(customerEntity -> this.vehicleReactive.findAllByCustomerAndLeaveDateIsNull(customerEntity)
                .map(VehicleEntity::toVehicle));
    }

    private Mono<VehicleEntity> findVehicleEntityByReference(String reference) {
        return vehicleReactive.findByReference(reference)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Reference: " + reference)));
    }

    @Override
    public Mono<Vehicle> findByReference(String reference) {
        return findVehicleEntityByReference(reference)
                .map(VehicleEntity::toVehicle);
    }

    private Mono<Void> assertBinNotExist(Vehicle vehicle){
        return vehicleReactive.findByBin(vehicle.getBin())
                .flatMap(vehicleEntity -> {
                            if (vehicle.getReference() != null && vehicle.getReference().equals(vehicleEntity.getReference())) {
                                return Mono.empty();
                            } else {
                                return Mono.error(
                                        new ConflictException("Exist already vehicle with bin: " + vehicleEntity.getBin()));
                            }
                        }
                );
    }

    private Mono<CustomerEntity> findCustomer(String identificationCustomer){
        return customerReactive.findByIdentificationId(identificationCustomer)
                .switchIfEmpty(Mono.error(new NotFoundException("Customer identification: " + identificationCustomer)));
    }

    private Mono<VehicleTypeEntity> findVehicleType(String vehicleTypeReference){
        return vehicleTypeReactive.findByReference(vehicleTypeReference)
                .switchIfEmpty(Mono.error(new NotFoundException("VehicleType reference: " + vehicleTypeReference)));
    }

    @Override
    public Mono<Vehicle> create(Vehicle vehicle) {
        VehicleEntity vehicleEntity = new VehicleEntity(vehicle);
        vehicleEntity.setFieldsCreation();

        return assertBinNotExist(vehicle)
                .then( findCustomer(vehicle.getIdentificationCustomer())
                            .map(customerEntity -> {
                                vehicleEntity.setCustomer(customerEntity);
                                return vehicleEntity;
                            })
                )
                .flatMap(vehicleEntity1 ->
                     findVehicleType(vehicle.getVehicleTypeReference())
                            .map(vehicleTypeEntity -> {
                                vehicleEntity1.setVehicleType(vehicleTypeEntity);
                                return vehicleEntity1;
                            })
                )
                .flatMap(vehicleEntity1 -> this.vehicleReactive.save(vehicleEntity1))
                .map(VehicleEntity::toVehicle);
    }

    @Override
    public Mono<Vehicle> update(Vehicle vehicle) {
        VehicleEntity vehicleEntity = new VehicleEntity(vehicle);

        return assertBinNotExist(vehicle)
                .then( findCustomer(vehicle.getIdentificationCustomer())
                        .map(customerEntity -> {
                            vehicleEntity.setCustomer(customerEntity);
                            return vehicleEntity;
                        })
                )
                .flatMap(vehicleEntity1 ->
                    findVehicleType(vehicle.getVehicleTypeReference())
                            .map(vehicleTypeEntity -> {
                                vehicleEntity1.setVehicleType(vehicleTypeEntity);
                                return vehicleEntity1;
                            })
                )
                .flatMap(vehicleEntity1 -> this.vehicleReactive.findByReference(vehicleEntity1.getReference()))
                .switchIfEmpty(Mono.error(new NotFoundException("Cannot update. Non existent vehicle " +
                        "with reference: " + vehicleEntity.getReference())))
                .flatMap(vehicleEntity1 -> {
                    vehicleEntity.setId(vehicleEntity1.getId());
                    vehicleEntity.setRegisterDate(vehicleEntity1.getRegisterDate());
                    vehicleEntity.setLastViewDate(vehicleEntity1.getLastViewDate());
                    return this.vehicleReactive.save(vehicleEntity);
                })
                .map(VehicleEntity::toVehicle);
    }

    @Override
    public Mono<Void> deleteLogic(String reference) {
        return findVehicleEntityByReference(reference)
                .flatMap(vehicleEntity -> {
                    vehicleEntity.setLeaveDate(LocalDateTime.now());
                    return vehicleReactive.save(vehicleEntity);
                })
                .then();
    }
}
