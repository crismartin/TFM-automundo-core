package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.infrastructure.api.dtos.TechnicianLineDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;

import static es.upm.miw.tfm.automundo.infrastructure.api.resources.TechnicianResource.*;
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
}
