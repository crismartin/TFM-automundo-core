package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.TestConfig;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.RevisionLineDto;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleLineDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import static es.upm.miw.tfm.automundo.infrastructure.api.resources.RevisionResource.REVISIONS;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.RevisionResource.VEHICLE_REFERENCE;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.VehicleResource.CUSTOMERS_IDENTIFICATION;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.VehicleResource.VEHICLES;

@RestTestConfig
public class RevisionResourceIT {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testFindVehiclesByIdCustomerOk() {
        String reference = "ref-1001";
        this.webTestClient
                .get()
                .uri(REVISIONS + VEHICLE_REFERENCE, reference)
                .exchange()
                .expectBodyList(RevisionLineDto.class)
                .value(Assertions::assertNotNull);
    }

    @Test
    void testFindVehiclesByIdCustomerUnknow() {
        String reference = "ref-unknow-rest";
        this.webTestClient
                .get()
                .uri(REVISIONS + VEHICLE_REFERENCE, reference)
                .exchange()
                .expectStatus().is4xxClientError();
    }
}
