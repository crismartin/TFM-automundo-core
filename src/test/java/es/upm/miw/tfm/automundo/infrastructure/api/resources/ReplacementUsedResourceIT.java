package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.Replacement;
import es.upm.miw.tfm.automundo.domain.model.ReplacementUsed;
import es.upm.miw.tfm.automundo.domain.model.Revision;
import es.upm.miw.tfm.automundo.domain.model.Technician;
import es.upm.miw.tfm.automundo.infrastructure.api.RestClientTestService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.ReplacementUsedDto;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.RevisionUpdateDto;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleLineDto;
import es.upm.miw.tfm.automundo.infrastructure.enums.StatusRevision;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static es.upm.miw.tfm.automundo.infrastructure.api.resources.ReplacementUsedResource.REVISION_REFERENCE;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.RevisionResource.REPLACEMENTS_USED;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.RevisionResource.REVISIONS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RestTestConfig
class ReplacementUsedResourceIT {

    @Autowired
    private WebTestClient webTestClient;
    @Autowired
    private RestClientTestService restClientTestService;


    @Test
    void testUpdateOk() {
        Replacement replacement = Replacement.builder()
                .reference("33333333")
                .build();

        ReplacementUsedDto replacementUsedDto = ReplacementUsedDto.builder()
                .reference("replacementUsed-ref-2")
                .discount(10).own(true).price(BigDecimal.valueOf(100.00)).quantity(1).replacement(replacement)
                .revisionReference("rev-1")
                .build();

        this.restClientTestService.loginAdmin(webTestClient)
                .put()
                .uri(REPLACEMENTS_USED)
                .body(Mono.just(replacementUsedDto), ReplacementUsedDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ReplacementUsed.class)
                .value(Assertions::assertNotNull)
                .value(replacementUsedUpdated -> {
                    assertNotNull(replacementUsedUpdated);
                    assertNotNull(replacementUsedUpdated.getReference());

                }).returnResult().getResponseBody();
    }

    @Test
    void testUpdateErrorByReplacementReferenceUnknown() {
        Replacement replacement = Replacement.builder()
                .reference("unknown")
                .build();

        ReplacementUsedDto replacementUsedDto = ReplacementUsedDto.builder()
                .reference("replacementUsed-ref-2")
                .discount(10).own(true).price(BigDecimal.valueOf(100.00)).quantity(1).replacement(replacement)
                .revisionReference("rev-1")
                .build();

        this.restClientTestService.loginAdmin(webTestClient)
                .put()
                .uri(REPLACEMENTS_USED)
                .body(Mono.just(replacementUsedDto), ReplacementUsedDto.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testUpdateErrorByRevisionReferenceUnknown() {
        Replacement replacement = Replacement.builder()
                .reference("33333333")
                .build();

        ReplacementUsedDto replacementUsedDto = ReplacementUsedDto.builder()
                .reference("replacementUsed-ref-2")
                .discount(10).own(true).price(BigDecimal.valueOf(100.00)).quantity(1).replacement(replacement)
                .revisionReference("unwknown")
                .build();

        this.restClientTestService.loginAdmin(webTestClient)
                .put()
                .uri(REPLACEMENTS_USED)
                .body(Mono.just(replacementUsedDto), ReplacementUsedDto.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testUpdateErrorByReferenceUnknown() {
        Replacement replacement = Replacement.builder()
                .reference("33333333")
                .build();

        ReplacementUsedDto replacementUsedDto = ReplacementUsedDto.builder()
                .reference("ref-unknown")
                .discount(10).own(true).price(BigDecimal.valueOf(100.00)).quantity(1).replacement(replacement)
                .revisionReference("rev-1")
                .build();

        this.restClientTestService.loginAdmin(webTestClient)
                .put()
                .uri(REPLACEMENTS_USED)
                .body(Mono.just(replacementUsedDto), ReplacementUsedDto.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testCreateOk() {
        Replacement replacement = Replacement.builder()
                .reference("33333333")
                .build();

        ReplacementUsedDto replacementUsedDto = ReplacementUsedDto.builder()
                .reference("replacementUsed-ref-2")
                .discount(10).own(true).price(BigDecimal.valueOf(100.00)).quantity(1).replacement(replacement)
                .revisionReference("rev-1")
                .build();

        this.restClientTestService.loginAdmin(webTestClient)
                .post()
                .uri(REPLACEMENTS_USED)
                .body(Mono.just(replacementUsedDto), ReplacementUsedDto.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(ReplacementUsed.class)
                .value(Assertions::assertNotNull)
                .value(replacementUsedCreated -> {
                    assertNotNull(replacementUsedCreated);
                    assertNotNull(replacementUsedCreated.getReference());
                    assertNotNull(replacementUsedCreated.getRevisionReference());
                    assertNotNull(replacementUsedCreated.getReplacementReference());

                    assertEquals(replacementUsedCreated.getQuantity(), replacementUsedCreated.getQuantity());
                    assertEquals(replacementUsedCreated.getDiscount(), replacementUsedCreated.getDiscount());
                    assertEquals(replacementUsedCreated.getPrice(), replacementUsedCreated.getPrice());
                    assertEquals(replacementUsedCreated.getOwn(), replacementUsedCreated.getOwn());
                    assertEquals(replacementUsedCreated.getReplacementReference(), replacementUsedCreated.getReplacementReference());
                    assertEquals(replacementUsedCreated.getRevisionReference(), replacementUsedCreated.getRevisionReference());

                }).returnResult().getResponseBody();
    }

    @Test
    void testCreateErrorByReplacementReferenceUnknown() {
        Replacement replacement = Replacement.builder()
                .reference("unknown")
                .build();

        ReplacementUsedDto replacementUsedDto = ReplacementUsedDto.builder()
                .reference("replacementUsed-ref-2")
                .discount(10).own(true).price(BigDecimal.valueOf(100.00)).quantity(1).replacement(replacement)
                .revisionReference("rev-1")
                .build();

        this.restClientTestService.loginAdmin(webTestClient)
                .post()
                .uri(REPLACEMENTS_USED)
                .body(Mono.just(replacementUsedDto), ReplacementUsedDto.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testCreateErrorByRevisionReferenceUnknown() {
        Replacement replacement = Replacement.builder()
                .reference("33333333")
                .build();

        ReplacementUsedDto replacementUsedDto = ReplacementUsedDto.builder()
                .reference("replacementUsed-ref-2")
                .discount(10).own(true).price(BigDecimal.valueOf(100.00)).quantity(1).replacement(replacement)
                .revisionReference("unwknown")
                .build();

        this.restClientTestService.loginAdmin(webTestClient)
                .post()
                .uri(REPLACEMENTS_USED)
                .body(Mono.just(replacementUsedDto), ReplacementUsedDto.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testFindAllByRevisionReferenceErrorByRevisionUnknown() {
        String revisionReference = "rev-unknown";
        this.restClientTestService.loginAdmin(webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(REPLACEMENTS_USED + REVISION_REFERENCE)
                        .queryParam("reference", revisionReference)
                        .build())
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    void testFindAllByRevisionReferenceOk() {
        String revisionReference = "rev-1";
        this.restClientTestService.loginAdmin(webTestClient)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(REPLACEMENTS_USED + REVISION_REFERENCE)
                        .queryParam("reference", revisionReference)
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(ReplacementUsed.class)
                .value(Assertions::assertNotNull);
    }
}
