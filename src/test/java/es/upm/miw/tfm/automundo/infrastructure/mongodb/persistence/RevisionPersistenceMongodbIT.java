package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.TestConfig;
import es.upm.miw.tfm.automundo.domain.persistence.RevisionPersistence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class RevisionPersistenceMongodbIT {

    @Autowired
    private RevisionPersistence revisionPersistence;

    @Test
    void testFindRevisionsByVehicleReference() {
        String vehicleReference = "ref-1001";

        StepVerifier
                .create(this.revisionPersistence.findAllByVehicleReference(vehicleReference))
                .expectNextMatches(revision -> {
                    assertNotNull(revision);
                    assertNotNull(revision.getReference());
                    assertNotNull(revision.getTechnician());
                    assertNotNull(revision.getVehicle());
                    assertEquals(vehicleReference, revision.getVehicleReference());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testFindRevisionsByVehicleReferenceUnknow() {
        String vehicleReference = "ref-unknow";

        StepVerifier
                .create(this.revisionPersistence.findAllByVehicleReference(vehicleReference))
                .expectError()
                .verify();
    }
}
