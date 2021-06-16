package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.*;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleLineDto;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleTypeDto;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleTypeLineDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static es.upm.miw.tfm.automundo.infrastructure.api.resources.ReplacementResource.REFERENCE;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.VehicleTypeResource.*;
import static org.junit.jupiter.api.Assertions.*;

@RestTestConfig
class VehicleTypeResourceIT {
    @Autowired
    private WebTestClient webTestClient;
    //@Autowired
    //private RestClientTestService restClientTestService;

    @Test
    void findByReferenceAndNameAndDescriptionAndActiveNullSafe() {
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
    void testFindByReferenceAndUpdate() {
        VehicleType vehicleTypeFound = this.webTestClient
                .get()
                .uri(VEHICLE_TYPES + REFERENCE, "11111111")
                .exchange()
                .expectStatus().isOk()
                .expectBody(VehicleType.class)
                .value(Assertions::assertNotNull)
                .value(vehicleType -> assertEquals("11111111", vehicleType.getReference()))
                .returnResult().getResponseBody();

        VehicleTypeUpdate vehicleTypeUpdate = new VehicleTypeUpdate();
        BeanUtils.copyProperties(vehicleTypeFound, vehicleTypeUpdate);
        vehicleTypeUpdate.setName("Tipo de vehículo Modificado");

        this.webTestClient
                .put()
                .uri(VEHICLE_TYPES + REFERENCE, "11111111")
                .body(Mono.just(vehicleTypeUpdate), VehicleTypeUpdate.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(VehicleType.class)
                .value(Assertions::assertNotNull)
                .value(updatedVehicleType -> {
                    assertEquals(vehicleTypeUpdate.getName(), updatedVehicleType.getName());
                    assertEquals(vehicleTypeFound.getDescription(), updatedVehicleType.getDescription());
                    assertEquals(vehicleTypeFound.getReference(), updatedVehicleType.getReference());
                    assertEquals(vehicleTypeFound.getActive(), updatedVehicleType.getActive());
                });
    }

    @Test
    void testUpdateNotFoundException() {
        VehicleTypeUpdate vehicleTypeUpdate = new VehicleTypeUpdate("Nombre", "Descripción", true);
        this.webTestClient
                .put()
                .uri(VEHICLE_TYPES + REFERENCE, "$$$$$$$$")
                .body(Mono.just(vehicleTypeUpdate), VehicleTypeUpdate.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testFindByReferenceNotFoundException() {
        this.webTestClient
                .get()
                .uri(VEHICLE_TYPES + REFERENCE, "$$$$$$$$")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testCreate() {
        VehicleTypeCreation vehicleTypeCreation = VehicleTypeCreation.builder().reference("99999999")
                .name("Vehículos de 3 ruedas").description("Vehículos especiales de 3 ruedas").build();
        this.webTestClient
                .post()
                .uri(VEHICLE_TYPES)
                .body(Mono.just(vehicleTypeCreation), VehicleTypeCreation.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(VehicleType.class)
                .value(Assertions::assertNotNull)
                .value(vehicleTypeCreated -> {
                    assertNotNull(vehicleTypeCreated.getId());
                    assertEquals("99999999", vehicleTypeCreated.getReference());
                    assertEquals("Vehículos de 3 ruedas", vehicleTypeCreated.getName());
                    assertEquals("Vehículos especiales de 3 ruedas", vehicleTypeCreated.getDescription());
                    assertTrue(vehicleTypeCreated.getActive());
                });
    }

    @Test
    void testCreateConflictReferenceException() {
        VehicleTypeCreation vehicleTypeCreation = VehicleTypeCreation.builder().reference("11111111")
                .name("Vehículos de 3 ruedas").description("Vehículos especiales de 3 ruedas").build();
        this.webTestClient
                .post()
                .uri(VEHICLE_TYPES)
                .body(Mono.just(vehicleTypeCreation), VehicleTypeCreation.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

    @Test
    void testFindAllActive() {
        this.webTestClient
                .get()
                .uri(VEHICLE_TYPES)
                .exchange()
                .expectBodyList(VehicleTypeDto.class)
                .value(Assertions::assertNotNull);
    }
}
