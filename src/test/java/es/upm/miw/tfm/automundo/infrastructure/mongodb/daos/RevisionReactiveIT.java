package es.upm.miw.tfm.automundo.infrastructure.mongodb.daos;

import es.upm.miw.tfm.automundo.TestConfig;
import es.upm.miw.tfm.automundo.domain.model.Revision;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.RevisionEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.VehicleEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class RevisionReactiveIT {

    @Autowired
    private RevisionReactive revisionReactive;

    @Test
    void testFindRevisionsByVehicleReferenceOk() {
        String vehicleId = "1lh67i9fds68h3d7809l982376mn";
        VehicleEntity vehicleEntity = VehicleEntity.builder().id(vehicleId).build();

        StepVerifier
                .create(this.revisionReactive.findAllByVehicleEntity(vehicleEntity))
                .expectNextMatches(revision -> {
                    assertNotNull(revision);
                    assertNotNull(revision.getReference());
                    assertNotNull(revision.getTechnicianEntity());
                    assertNotNull(revision.getVehicleEntity());
                    assertEquals(vehicleId, revision.getVehicleEntity().getId());
                    return true;
                })
                .thenCancel()
                .verify();
    }

}
