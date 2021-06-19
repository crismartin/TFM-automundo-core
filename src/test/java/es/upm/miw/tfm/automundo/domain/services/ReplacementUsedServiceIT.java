package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.TestConfig;
import es.upm.miw.tfm.automundo.domain.model.Replacement;
import es.upm.miw.tfm.automundo.domain.model.ReplacementUsed;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class ReplacementUsedServiceIT {

    @Autowired
    private ReplacementUsedService replacementUsedService;

    @Test
    void testUpdateOk() {
        Replacement replacement = Replacement.builder().reference("11111111").build();

        ReplacementUsed replacementUsed = ReplacementUsed.builder()
                .reference("replacementUsed-ref-1")
                .quantity(1).discount(10).price(BigDecimal.valueOf(67.95)).own(true)
                .replacement(replacement)
                .revisionReference("rev-1")
                .build();

        StepVerifier
                .create(this.replacementUsedService.update(replacementUsed))
                .expectNextMatches(replacementUsedUpdated -> {
                    assertNotNull(replacementUsedUpdated);
                    assertNotNull(replacementUsedUpdated.getReference());
                    assertNotNull(replacementUsedUpdated.getRevisionReference());
                    assertNotNull(replacementUsedUpdated.getReplacementReference());

                    assertEquals(replacementUsed.getQuantity(), replacementUsedUpdated.getQuantity());
                    assertEquals(replacementUsed.getDiscount(), replacementUsedUpdated.getDiscount());
                    assertEquals(replacementUsed.getPrice(), replacementUsedUpdated.getPrice());
                    assertEquals(replacementUsed.getOwn(), replacementUsedUpdated.getOwn());
                    assertEquals(replacementUsed.getReplacementReference(), replacementUsedUpdated.getReplacementReference());
                    assertEquals(replacementUsed.getRevisionReference(), replacementUsedUpdated.getRevisionReference());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testUpdateErrorByRevisionUnknown() {
        Replacement replacement = Replacement.builder().reference("11111111").build();

        ReplacementUsed replacementUsed = ReplacementUsed.builder()
                .reference("replacementUsed-ref-1")
                .quantity(1).discount(10).price(BigDecimal.valueOf(67.95)).own(true)
                .replacement(replacement)
                .revisionReference("rev-unknown")
                .build();

        StepVerifier
                .create(this.replacementUsedService.update(replacementUsed))
                .expectError()
                .verify();
    }

    @Test
    void testUpdateErrorByReplacementUnknown() {
        Replacement replacement = Replacement.builder().reference("unknown").build();

        ReplacementUsed replacementUsed = ReplacementUsed.builder()
                .reference("replacementUsed-ref-1")
                .quantity(1).discount(10).price(BigDecimal.valueOf(67.95)).own(true)
                .replacement(replacement)
                .revisionReference("rev-1")
                .build();

        StepVerifier
                .create(this.replacementUsedService.update(replacementUsed))
                .expectError()
                .verify();
    }
}