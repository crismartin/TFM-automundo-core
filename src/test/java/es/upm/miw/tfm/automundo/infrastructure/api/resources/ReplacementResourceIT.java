package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.*;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.ReplacementLineDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

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
    void testFindByReferenceAndUpdate() {
        Replacement replacementFound = this.webTestClient
                .get()
                .uri(REPLACEMENTS + REFERENCE, "11111111")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Replacement.class)
                .value(Assertions::assertNotNull)
                .value(replacement -> assertEquals("11111111", replacement.getReference()))
                .returnResult().getResponseBody();

        ReplacementUpdate replacementUpdate = new ReplacementUpdate();
        BeanUtils.copyProperties(replacementFound, replacementUpdate);
        replacementUpdate.setName("Repuesto Modificado");
        replacementUpdate.setPrice(new BigDecimal(28.5));

        this.webTestClient
                .put()
                .uri(REPLACEMENTS + REFERENCE, "11111111")
                .body(Mono.just(replacementUpdate), ReplacementUpdate.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Replacement.class)
                .value(Assertions::assertNotNull)
                .value(updatedReplacement -> {
                    assertEquals(replacementUpdate.getName(), updatedReplacement.getName());
                    assertEquals(replacementUpdate.getPrice(), updatedReplacement.getPrice());
                    assertEquals(replacementFound.getDescription(), updatedReplacement.getDescription());
                    assertEquals(replacementFound.getReference(), replacementFound.getReference());
                });
    }

    @Test
    void testFindByReferenceNotFoundException() {
        this.webTestClient
                .get()
                .uri(REPLACEMENTS + REFERENCE, "$$$$$$$$")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testCreateAndDelete() {
        ReplacementCreation replacementCreation = ReplacementCreation.builder().reference("99999999")
                .name("Amortiguadores").price(new BigDecimal(69.99))
                .description("Amortiguadores para coche AUDI A3").build();
        Replacement replacement = this.webTestClient
                .post()
                .uri(REPLACEMENTS)
                .body(Mono.just(replacementCreation), ReplacementCreation.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Replacement.class)
                .value(Assertions::assertNotNull)
                .value(replacementCreated -> {
                    assertNotNull(replacementCreated.getId());
                    assertEquals("99999999", replacementCreated.getReference());
                    assertEquals("Amortiguadores", replacementCreated.getName());
                    assertEquals(new BigDecimal(69.99).toString(), replacementCreated.getPrice().toString());
                    assertEquals("Amortiguadores para coche AUDI A3", replacementCreated.getDescription());
                }).returnResult().getResponseBody();
        assertNotNull(replacement);

        this.webTestClient
                .delete()
                .uri(REPLACEMENTS + REFERENCE, "99999999")
                .exchange()
                .expectStatus().isOk();

        this.webTestClient
                .get()
                .uri(REPLACEMENTS + REFERENCE, "99999999")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);

    }
    //TODO: make a test deleting conflict exception when replacement is used in any revision of any vehicle

    @Test
    void testDeleteNotFoundException() {
        this.webTestClient
                .delete()
                .uri(REPLACEMENTS + REFERENCE, "$$$$$$$$")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testCreateConflictReferenceException() {
        ReplacementCreation replacementCreation = ReplacementCreation.builder().reference("11111111")
                .name("Amortiguadores").price(new BigDecimal(69.99))
                .description("Amortiguadores para coche AUDI A3").build();
        this.webTestClient
                .post()
                .uri(REPLACEMENTS)
                .body(Mono.just(replacementCreation), ReplacementCreation.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }
}
