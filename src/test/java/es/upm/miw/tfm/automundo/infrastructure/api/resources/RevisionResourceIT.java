package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.TestConfig;
import es.upm.miw.tfm.automundo.domain.model.Revision;
import es.upm.miw.tfm.automundo.domain.model.Technician;
import es.upm.miw.tfm.automundo.domain.model.Vehicle;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.*;
import es.upm.miw.tfm.automundo.infrastructure.enums.StatusRevision;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import static es.upm.miw.tfm.automundo.infrastructure.api.resources.RevisionResource.REVISIONS;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.RevisionResource.VEHICLE_REFERENCE;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.VehicleResource.CUSTOMERS_IDENTIFICATION;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.VehicleResource.VEHICLES;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RestTestConfig
class RevisionResourceIT {

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

    @Test
    void testCreateOk() {
        TechnicianLineDto technician = TechnicianLineDto.builder()
                .identificationId("11111111-T")
                .build();

        RevisionNewDto revisionNewDto = RevisionNewDto.builder()
                .vehicleReference("ref-2002")
                .diagnostic("DIAGNOSTIC RESOURCE")
                .registerDate(LocalDateTime.now())
                .initialKilometers(1000)
                .workedHours(10)
                .workDescription("DESCRIPTION RESOURCE TEST")
                .technician(technician)
                .build();

        this.webTestClient
                .post()
                .uri(REVISIONS)
                .body(Mono.just(revisionNewDto), RevisionNewDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Revision.class)
                .value(Assertions::assertNotNull)
                .value(revisionCreated -> {
                    assertNotNull(revisionCreated);
                    assertNotNull(revisionCreated.getReference());

                    assertEquals(StatusRevision.POR_CONFIRMAR, revisionCreated.getStatus());
                    assertEquals(revisionCreated.getTechnicianIdentification(), revisionNewDto.getTechnicianIdentification());
                    assertEquals(revisionCreated.getVehicleReference(), revisionNewDto.getVehicleReference());
                }).returnResult().getResponseBody();
    }

    @Test
    void testCreateErrorByTechnicianUnknown() {
        TechnicianLineDto technician = TechnicianLineDto.builder()
                .identificationId("TECHNICIAN_UNKNOWN")
                .build();

        RevisionNewDto revisionNewDto = RevisionNewDto.builder()
                .vehicleReference("ref-2002")
                .diagnostic("DIAGNOSTIC RESOURCE")
                .registerDate(LocalDateTime.now())
                .initialKilometers(1000)
                .workedHours(10)
                .workDescription("DESCRIPTION RESOURCE TEST")
                .technician(technician)
                .build();

        this.webTestClient
                .post()
                .uri(REVISIONS)
                .body(Mono.just(revisionNewDto), RevisionNewDto.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testCreateErrorByVehicleUnknown() {
        TechnicianLineDto technician = TechnicianLineDto.builder()
                .identificationId("11111111-T")
                .build();

        RevisionNewDto revisionNewDto = RevisionNewDto.builder()
                .vehicleReference("VEHICLE_REF_UNKNOWN")
                .diagnostic("DIAGNOSTIC RESOURCE")
                .registerDate(LocalDateTime.now())
                .initialKilometers(1000)
                .workedHours(10)
                .workDescription("DESCRIPTION RESOURCE TEST")
                .technician(technician)
                .build();

        this.webTestClient
                .post()
                .uri(REVISIONS)
                .body(Mono.just(revisionNewDto), RevisionNewDto.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

}
