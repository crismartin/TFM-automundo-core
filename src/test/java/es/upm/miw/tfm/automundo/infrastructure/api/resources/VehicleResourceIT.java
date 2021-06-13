package es.upm.miw.tfm.automundo.infrastructure.api.resources;


import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleDto;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleLineDto;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleNewDto;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleTypeDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.VehicleResource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@RestTestConfig
class VehicleResourceIT {
    private static final String IDENTIFICATION_CUSTOMER_CREATION = "33333333-A";
    private static final String REFERENCE_VEHICLE_TYPE = "11111111";
    private static final String REFERENCE_VEHICLE = "ref-2002";

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

    @Test
    void testCreateOk() {
        VehicleTypeDto vehicleType = VehicleTypeDto.builder()
                .reference(REFERENCE_VEHICLE_TYPE)
                .build();

        VehicleNewDto vehicleCreation = VehicleNewDto.builder()
                .model("MERCEDES BENZ CLASE A").yearRelease(2020)
                .plate("MBA-202").bin("MB-CA").vehicleType(vehicleType)
                .typeNumber("GOB-111")
                .identificationCustomer(IDENTIFICATION_CUSTOMER_CREATION)
                .build();

        this.webTestClient
                .post()
                .uri(VEHICLES)
                .body(Mono.just(vehicleCreation), VehicleNewDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(VehicleDto.class)
                .value(Assertions::assertNotNull)
                .value(vehicleCreated -> {
                    assertNotNull(vehicleCreated.getReference());
                    assertNotNull(vehicleCreated.getCustomer());
                    assertNotNull(vehicleCreated.getYearRelease());
                    assertNotNull(vehicleCreated.getLastViewDate());

                    assertEquals(vehicleCreation.getIdentificationCustomer(), vehicleCreated.getIdentificationCustomer());
                    assertEquals(vehicleCreation.getVehicleTypeReference(), vehicleCreated.getVehicleTypeReference());
                    assertEquals(vehicleCreation.getBin(), vehicleCreated.getBin());
                    assertEquals(vehicleCreation.getModel(), vehicleCreated.getModel());
                    assertEquals(vehicleCreation.getTypeNumber(), vehicleCreated.getTypeNumber());
                    assertEquals(vehicleCreation.getPlate(), vehicleCreated.getPlate());
                }).returnResult().getResponseBody();
    }

    @Test
    void testCreateErrorByBinAlreadyExist() {
        VehicleTypeDto vehicleType = VehicleTypeDto.builder()
                .reference(REFERENCE_VEHICLE_TYPE)
                .build();

        VehicleNewDto vehicleCreation = VehicleNewDto.builder()
                .model("MERCEDES BENZ CLASE A").yearRelease(2020)
                .plate("MBA-202").bin("vh-1001").vehicleType(vehicleType)
                .typeNumber("GOB-111")
                .identificationCustomer(IDENTIFICATION_CUSTOMER_CREATION)
                .build();

        this.webTestClient
                .post()
                .uri(VEHICLES)
                .body(Mono.just(vehicleCreation), VehicleNewDto.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testCreateErrorByVehicleTypeUnknow() {
        VehicleTypeDto vehicleType = VehicleTypeDto.builder()
                .reference("0")
                .build();

        VehicleNewDto vehicleCreation = VehicleNewDto.builder()
                .model("MERCEDES BENZ CLASE A").yearRelease(2020)
                .plate("MBA-202").bin("vh-1001").vehicleType(vehicleType)
                .typeNumber("GOB-111")
                .identificationCustomer(IDENTIFICATION_CUSTOMER_CREATION)
                .build();

        this.webTestClient
                .post()
                .uri(VEHICLES)
                .body(Mono.just(vehicleCreation), VehicleNewDto.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testCreateErrorByCustomerUnknow() {
        VehicleTypeDto vehicleType = VehicleTypeDto.builder()
                .reference(REFERENCE_VEHICLE_TYPE)
                .build();

        VehicleNewDto vehicleCreation = VehicleNewDto.builder()
                .model("MERCEDES BENZ CLASE A").yearRelease(2020)
                .plate("MBA-202").bin("vh-1001").vehicleType(vehicleType)
                .typeNumber("GOB-111")
                .identificationCustomer("Dummy1234")
                .build();

        this.webTestClient
                .post()
                .uri(VEHICLES)
                .body(Mono.just(vehicleCreation), VehicleNewDto.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testUpdateOk() {
        VehicleTypeDto vehicleType = VehicleTypeDto.builder()
                .reference(REFERENCE_VEHICLE_TYPE)
                .build();

        VehicleNewDto vehicle = VehicleNewDto.builder()
                .model("TEST RESOURCE").yearRelease(2034)
                .plate("PLT-TST").bin("TST").vehicleType(vehicleType)
                .typeNumber("GOB-TST")
                .identificationCustomer(IDENTIFICATION_CUSTOMER_CREATION)
                .build();

        this.webTestClient
                .put()
                .uri(VEHICLES + "/" + REFERENCE_VEHICLE)
                .body(Mono.just(vehicle), VehicleNewDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(VehicleDto.class)
                .value(Assertions::assertNotNull)
                .value(vehicleUpdated -> {
                    assertNotNull(vehicleUpdated.getReference());
                    assertNotNull(vehicleUpdated.getCustomer());
                    assertNotNull(vehicleUpdated.getYearRelease());
                    assertNotNull(vehicleUpdated.getLastViewDate());

                    assertEquals(REFERENCE_VEHICLE, vehicleUpdated.getReference());
                    assertEquals(vehicle.getIdentificationCustomer(), vehicleUpdated.getIdentificationCustomer());
                    assertEquals(vehicle.getVehicleTypeReference(), vehicleUpdated.getVehicleTypeReference());
                    assertEquals(vehicle.getBin(), vehicleUpdated.getBin());
                    assertEquals(vehicle.getModel(), vehicleUpdated.getModel());
                    assertEquals(vehicle.getTypeNumber(), vehicleUpdated.getTypeNumber());
                    assertEquals(vehicle.getPlate(), vehicleUpdated.getPlate());
                    assertEquals(vehicle.getYearRelease(), vehicleUpdated.getYearRelease());
                }).returnResult().getResponseBody();
    }
}
