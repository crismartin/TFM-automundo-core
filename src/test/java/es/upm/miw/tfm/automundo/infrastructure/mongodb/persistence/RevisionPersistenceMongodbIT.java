package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.TestConfig;
import es.upm.miw.tfm.automundo.domain.model.*;
import es.upm.miw.tfm.automundo.domain.persistence.RevisionPersistence;
import es.upm.miw.tfm.automundo.infrastructure.enums.StatusRevision;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

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

    @Test
    void testCreateOk(){

        Technician technician = Technician.builder()
                .identificationId("11111111-T")
                .build();

        Vehicle vehicle = Vehicle.builder()
                .reference("ref-2002")
                .build();

        Revision revision = Revision.builder()
                .diagnostic("REVISION DIAGNOSTIC")
                .registerDate(LocalDateTime.now())
                .initialKilometers(1000)
                .workedHours(10)
                .vehicle(vehicle)
                .technician(technician)
                .build();

        StepVerifier
                .create(this.revisionPersistence.create(revision))
                .expectNextMatches(revisionCreated -> {
                    assertNotNull(revisionCreated);
                    assertNotNull(revisionCreated.getReference());
                    assertEquals(StatusRevision.POR_CONFIRMAR, revisionCreated.getStatus());
                    assertEquals(revisionCreated.getTechnicianIdentification(), revision.getTechnicianIdentification());
                    assertEquals(revisionCreated.getVehicleReference(), revision.getVehicleReference());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testCreateErrorByVehicleUnknow(){

        Technician technician = Technician.builder()
                .identificationId("11111111-T")
                .build();

        Vehicle vehicle = Vehicle.builder()
                .reference("VEHICLE_UNKNOW_DUMMY")
                .build();

        Revision revision = Revision.builder()
                .diagnostic("REVISION DIAGNOSTIC")
                .registerDate(LocalDateTime.now())
                .initialKilometers(1000)
                .workedHours(10)
                .vehicle(vehicle)
                .technician(technician)
                .build();

        StepVerifier
                .create(this.revisionPersistence.create(revision))
                .expectError()
                .verify();
    }

    @Test
    void testCreateErrorByTechnicianUnknow(){

        Technician technician = Technician.builder()
                .identificationId("TECHNICIAN_UNKNOW")
                .build();

        Vehicle vehicle = Vehicle.builder()
                .reference("ref-2002")
                .build();

        Revision revision = Revision.builder()
                .diagnostic("REVISION DIAGNOSTIC")
                .registerDate(LocalDateTime.now())
                .initialKilometers(1000)
                .workedHours(10)
                .vehicle(vehicle)
                .technician(technician)
                .build();

        StepVerifier
                .create(this.revisionPersistence.create(revision))
                .expectError()
                .verify();
    }
}
