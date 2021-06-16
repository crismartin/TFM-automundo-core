package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.*;
import es.upm.miw.tfm.automundo.infrastructure.api.RestClientTestService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.TechnicianLineDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static es.upm.miw.tfm.automundo.infrastructure.api.resources.TechnicianResource.*;
import static org.junit.jupiter.api.Assertions.*;

@RestTestConfig
public class TechnicianResourceIT {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private RestClientTestService restClientTestService;

    @Test
    void findByIdentificationIdAndNameAndSurNameAndActiveNullSafe() {
        this.restClientTestService.loginAdmin(webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(TECHNICIANS + SEARCH)
                        .queryParam("surName", "López")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(TechnicianLineDto.class)
                .value(Assertions::assertNotNull)
                .value(technicianLines -> assertTrue(technicianLines
                        .stream().allMatch(technicianLine -> technicianLine.getCompleteName().toLowerCase().contains("lópez"))));
    }

    @Test
    void testFindByIdentificationIdAndUpdateSetNotActive() {
        Technician technicianFound = this.restClientTestService.loginAdmin(webTestClient)
                .get()
                .uri(TECHNICIANS + IDENTIFICATION_ID, "11111111-T")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Technician.class)
                .value(Assertions::assertNotNull)
                .value(technician -> assertEquals("11111111-T", technician.getIdentificationId()
                )).returnResult().getResponseBody();

        TechnicianUpdate technicianUpdate = new TechnicianUpdate();
        BeanUtils.copyProperties(technicianFound, technicianUpdate);
        technicianUpdate.setActive(false);
        technicianUpdate.setName("Antonio");

        this.restClientTestService.loginAdmin(webTestClient)
                .put()
                .uri(TECHNICIANS + IDENTIFICATION_ID, "11111111-T")
                .body(Mono.just(technicianUpdate), TechnicianUpdate.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Technician.class)
                .value(Assertions::assertNotNull)
                .value(updatedTechnician -> {
                    assertEquals(technicianUpdate.getActive(), updatedTechnician.getActive());
                    assertEquals(technicianUpdate.getName(), updatedTechnician.getName());
                    assertNotNull(updatedTechnician.getLeaveDate());
                    assertEquals(technicianFound.getIdentificationId(), updatedTechnician.getIdentificationId());
                    assertEquals(technicianFound.getMobile(), updatedTechnician.getMobile());
                    assertEquals(technicianFound.getSsNumber(), updatedTechnician.getSsNumber());
                    assertEquals(technicianFound.getSurName(), updatedTechnician.getSurName());
                    assertEquals(technicianFound.getSecondSurName(), updatedTechnician.getSecondSurName());
                    assertEquals(technicianFound.getRegistrationDate(), updatedTechnician.getRegistrationDate());
                });
    }

    @Test
    void testFindByIdentificationIdAndUpdateSetActive() {
        Technician technicianFound = this.restClientTestService.loginAdmin(webTestClient)
                .get()
                .uri(TECHNICIANS + IDENTIFICATION_ID, "44444444-T")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Technician.class)
                .value(Assertions::assertNotNull)
                .value(technician -> assertEquals("44444444-T", technician.getIdentificationId()
                )).returnResult().getResponseBody();

        TechnicianUpdate technicianUpdate = new TechnicianUpdate();
        BeanUtils.copyProperties(technicianFound, technicianUpdate);
        technicianUpdate.setActive(true);
        technicianUpdate.setName("Borja");

        this.restClientTestService.loginAdmin(webTestClient)
                .put()
                .uri(TECHNICIANS + IDENTIFICATION_ID, "44444444-T")
                .body(Mono.just(technicianUpdate), TechnicianUpdate.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Technician.class)
                .value(Assertions::assertNotNull)
                .value(updatedTechnician -> {
                    assertEquals(technicianUpdate.getActive(), updatedTechnician.getActive());
                    assertEquals(technicianUpdate.getName(), updatedTechnician.getName());
                    assertNull(updatedTechnician.getLeaveDate());
                    assertEquals(technicianFound.getIdentificationId(), updatedTechnician.getIdentificationId());
                    assertEquals(technicianFound.getMobile(), updatedTechnician.getMobile());
                    assertEquals(technicianFound.getSsNumber(), updatedTechnician.getSsNumber());
                    assertEquals(technicianFound.getSurName(), updatedTechnician.getSurName());
                    assertEquals(technicianFound.getSecondSurName(), updatedTechnician.getSecondSurName());
                    assertEquals(technicianFound.getRegistrationDate(), updatedTechnician.getRegistrationDate());
                });
    }

    @Test
    void testUpdateNotFoundException() {
        TechnicianUpdate technicianUpdate =
                new TechnicianUpdate("test", "test", "test", "test", "test", false);

        this.restClientTestService.loginAdmin(webTestClient)
                .put()
                .uri(TECHNICIANS + IDENTIFICATION_ID, "$$$$$$$$-T")
                .body(Mono.just(technicianUpdate), TechnicianUpdate.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testFindByIdentificationIdNotFoundException() {
        this.restClientTestService.loginAdmin(webTestClient)
                .get()
                .uri(TECHNICIANS + IDENTIFICATION_ID, "$$$$$$$$")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testCreate() {
        TechnicianCreation technicianCreation = TechnicianCreation.builder().identificationId("99999999-T")
                .ssNumber("SS-9999999").mobile("654744344")
                .name("Sergio").surName("Vázquez").secondSurName("Sánchez").build();
        this.restClientTestService.loginAdmin(webTestClient)
                .post()
                .uri(TECHNICIANS)
                .body(Mono.just(technicianCreation), TechnicianCreation.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Technician.class)
                .value(Assertions::assertNotNull)
                .value(technicianCreated -> {
                    assertNotNull(technicianCreated.getId());
                    assertEquals("99999999-T", technicianCreated.getIdentificationId());
                    assertEquals("SS-9999999", technicianCreated.getSsNumber());
                    assertEquals("654744344", technicianCreated.getMobile());
                    assertEquals("Sergio", technicianCreated.getName());
                    assertEquals("Vázquez", technicianCreated.getSurName());
                    assertEquals("Sánchez", technicianCreated.getSecondSurName());
                    assertTrue(technicianCreated.getActive());
                });
    }

    @Test
    void testConflictIdentificationIdException() {
        TechnicianCreation technicianCreation = TechnicianCreation.builder().identificationId("11111111-T")
                .ssNumber("SS-9999999").mobile("654744344")
                .name("Sergio").surName("Vázquez").secondSurName("Sánchez").build();
        this.restClientTestService.loginAdmin(webTestClient)
                .post()
                .uri(TECHNICIANS)
                .body(Mono.just(technicianCreation), TechnicianCreation.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

}
