package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleDto;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleLineDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.VehicleResource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;


@RestTestConfig
class VehicleResourceIT {
    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testFindVehiclesByIdCustomer() {
        String identificationCustomer = "22222222-A";
        this.webTestClient
                .get()
                .uri(VEHICLES + CUSTOMERS_IDENTIFICATION, identificationCustomer)
                .exchange()
                .expectBodyList(VehicleLineDto.class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void testFindByReference() {
        String referenceVehicle = "ref-1001";
        this.webTestClient
                .get()
                .uri(VEHICLES + REFERENCE , referenceVehicle)
                .exchange()
                .expectBody(VehicleDto.class)
                .value(Assertions::assertNotNull)
                .value(vehicle -> assertEquals(referenceVehicle, vehicle.getReference()))
                .returnResult()
                .getResponseBody();
    }
}
