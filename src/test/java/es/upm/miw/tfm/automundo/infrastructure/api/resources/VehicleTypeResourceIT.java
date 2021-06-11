package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleTypeLineDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static es.upm.miw.tfm.automundo.infrastructure.api.resources.VehicleTypeResource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RestTestConfig
public class VehicleTypeResourceIT {
    @Autowired
    private WebTestClient webTestClient;
    //@Autowired
    //private RestClientTestService restClientTestService;

    @Test
    void findByReferenceAndNameAndDescriptionNullSafe() {
        this.webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(VEHICLE_TYPES + SEARCH)
                        .queryParam("name", "gobierno")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(VehicleTypeLineDto.class)
                .value(Assertions::assertNotNull)
                .value(vehicleTypeLines ->
                        assertTrue(vehicleTypeLines
                                .stream().allMatch(vehicleTypeLine -> vehicleTypeLine.getName().toLowerCase().contains("gobierno"))));
    }

    @Test
    void testFindByReference() {
        this.webTestClient
                .get()
                .uri(VEHICLE_TYPES + REFERENCE, "11111111")
                .exchange()
                .expectStatus().isOk()
                .expectBody(VehicleType.class)
                .value(Assertions::assertNotNull)
                .value(vehicleType -> assertEquals("11111111", vehicleType.getReference()));
    }

    @Test
    void testFindByReferenceNotFoundException() {
        this.webTestClient
                .get()
                .uri(VEHICLE_TYPES + REFERENCE, "$$$$$$$$")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }
}
