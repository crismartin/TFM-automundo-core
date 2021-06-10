package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.Replacement;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.ReplacementLineDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import static es.upm.miw.tfm.automundo.infrastructure.api.resources.CustomerResource.CUSTOMERS;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.CustomerResource.IDENTIFICATION_ID;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.ReplacementResource.*;
import static org.junit.jupiter.api.Assertions.*;

@RestTestConfig
public class ReplacementResourceIT {
    @Autowired
    private WebTestClient webTestClient;
    //@Autowired
    //private RestClientTestService restClientTestService;

    @Test
    void findByReferenceAndNameAndDescriptionNullSafe() {
        this.webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(REPLACEMENTS + SEARCH)
                        .queryParam("name", "freno")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ReplacementLineDto.class)
                .value(Assertions::assertNotNull)
                .value(replacementLines ->
                    assertTrue(replacementLines
                        .stream().allMatch(replacementLine -> replacementLine.getName().toLowerCase().contains("freno"))));
    }

    @Test
    void testFindByReference() {
        this.webTestClient
                .get()
                .uri(REPLACEMENTS + REFERENCE, "11111111")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Replacement.class)
                .value(Assertions::assertNotNull)
                .value(replacement -> assertEquals("11111111", replacement.getReference()));
    }

    @Test
    void testFindByReferenceNotFoundException() {
        this.webTestClient
                .get()
                .uri(REPLACEMENTS + REFERENCE, "$$$$$$$$")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }
}
