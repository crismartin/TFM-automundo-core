package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.Technician;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.TechnicianLineDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static es.upm.miw.tfm.automundo.infrastructure.api.resources.TechnicianResource.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RestTestConfig
public class TechnicianResourceIT {

    @Autowired
    private WebTestClient webTestClient;
    //@Autowired
    //private RestClientTestService restClientTestService;

    @Test
    void findByIdentificationIdAndNameAndSurNameAndActiveNullSafe() {
        this.webTestClient
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
    void testFindByIdentificationId() {
        this.webTestClient
                .get()
                .uri(TECHNICIANS + IDENTIFICATION_ID, "11111111-T")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Technician.class)
                .value(Assertions::assertNotNull)
                .value(technician -> assertEquals("11111111-T", technician.getIdentificationId()
                ));
    }

    @Test
    void testFindByIdentificationIdNotFoundException() {
        this.webTestClient
                .get()
                .uri(TECHNICIANS + IDENTIFICATION_ID, "$$$$$$$$")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

}
