package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.TestConfig;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.ReplacementUsedEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.RevisionEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class ReplacementUsedReactiveIT {

    @Autowired
    private ReplacementUsedReactive replacementUsedReactive;

    @Test
    void testFindAllByRevisionEntityOk() {

        RevisionEntity revisionEntity = RevisionEntity.builder().id("revision-id-1").build();

        StepVerifier
                .create(this.replacementUsedReactive.findAllByRevisionEntity(revisionEntity))
                .expectNextMatches(replacementUsedEntity -> {
                    assertNotNull(replacementUsedEntity);
                    assertNotNull(replacementUsedEntity.getId());

                    assertEquals(revisionEntity.getId(), replacementUsedEntity.getRevisionEntity().getId());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void findByReference() {
        String replacementUsedReference = "replacementUsed-ref-2";
        StepVerifier
                .create(this.replacementUsedReactive.findByReference(replacementUsedReference))
                .expectNextMatches(replacementUsedEntity -> {
                    assertNotNull(replacementUsedEntity);
                    assertNotNull(replacementUsedEntity.getId());

                    assertEquals(replacementUsedReference, replacementUsedEntity.getReference());
                    return true;
                })
                .thenCancel()
                .verify();
    }

}
