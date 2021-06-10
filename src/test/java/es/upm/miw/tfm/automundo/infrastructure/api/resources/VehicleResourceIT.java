package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleLineDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.VehicleResource.*;


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
}
