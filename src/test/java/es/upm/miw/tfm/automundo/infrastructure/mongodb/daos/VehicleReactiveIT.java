package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.TestConfig;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.CustomerEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.*;


@TestConfig
class VehicleReactiveIT {

    @Autowired
    private VehicleReactive vehicleReactive;

    @Test
    void testFindVehiclesByIdCustomer() {
        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId("id_customer_2");

        StepVerifier
                .create(this.vehicleReactive.findAllByCustomerAndLeaveDateIsNull(customerEntity))
                .expectNextMatches(vehicleEntity -> {
                    assertNotNull(vehicleEntity);
                    assertEquals(vehicleEntity.getIdCustomer(), customerEntity.getId());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testFindByReference(){
        String referenceVehicle = "ref-1001";
        StepVerifier
                .create(this.vehicleReactive.findByReference(referenceVehicle))
                .expectNextMatches(vehicle -> {
                    assertNotNull(vehicle);
                    assertEquals(vehicle.getReference(), referenceVehicle);
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testFindAllByBinExistAndPlateNullOk() {
        String bin = "cp-209";

        StepVerifier
                .create(this.vehicleReactive.findAllByBinAndPlateNullSafe(bin, null))
                .expectNextMatches(vehicleEntity -> {
                    assertNotNull(vehicleEntity);
                    assertEquals(vehicleEntity.getBin(), bin);
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testFindAllByBinNullAndPlateNotExistOk() {
        String plate = "unknown";

        StepVerifier
                .create(this.vehicleReactive.findAllByBinAndPlateNullSafe(null, plate))
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void testFindAllByBinNullAndPlateNullOk() {
        StepVerifier
                .create(this.vehicleReactive.findAllByBinAndPlateNullSafe(null, null))
                .expectNextMatches(vehicleEntity -> {
                    assertNotNull(vehicleEntity);
                    assertNotNull(vehicleEntity.getId());
                    return true;
                })
                .thenCancel()
                .verify();
    }
}
