package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.TestConfig;
import es.upm.miw.tfm.automundo.domain.persistence.VehiclePersistence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class VehiclePersistenceMongodbIT {
    @Autowired
    private VehiclePersistence vehiclePersistence;

    @Test
    void testFindVehiclesByIdCustomer() {
        String identificationCustomer = "22222222-A";

        StepVerifier
                .create(this.vehiclePersistence.findVehiclesByIdCustomer(identificationCustomer))
                .expectNextMatches(vehicle -> {
                    assertNotNull(vehicle);
                    assertEquals(vehicle.getIdentificationCustomer(), identificationCustomer);
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testFindByReference(){
        String referenceVehicle = "ref-1001";
        StepVerifier
                .create(this.vehiclePersistence.findByReference(referenceVehicle))
                .expectNextMatches(vehicle -> {
                    assertNotNull(vehicle);
                    assertEquals(vehicle.getReference(), referenceVehicle);
                    return true;
                })
                .thenCancel()
                .verify();
    }
}
