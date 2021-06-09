package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class VehicleServiceIT {

    @Autowired
    private VehicleService vehicleService;

    @Test
    void testFindVehiclesByIdCustomer() {
        String identificationCustomer = "22222222-A";

        StepVerifier
                .create(this.vehicleService.findVehiclesByIdCustomer(identificationCustomer))
                .expectNextMatches(vehicle -> {
                    assertNotNull(vehicle);
                    assertEquals(vehicle.getIdentificationCustomer(), identificationCustomer);
                    return true;
                })
                .thenCancel()
                .verify();
    }
}
