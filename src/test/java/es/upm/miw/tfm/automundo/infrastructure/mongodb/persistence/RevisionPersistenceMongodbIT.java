package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.TestConfig;
import es.upm.miw.tfm.automundo.domain.model.*;
import es.upm.miw.tfm.automundo.domain.persistence.RevisionPersistence;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.ReplacementsUsedNewDto;
import es.upm.miw.tfm.automundo.infrastructure.enums.StatusRevision;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.ReplacementUsedEntity;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.RevisionEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static es.upm.miw.tfm.automundo.infrastructure.api.resources.RevisionResource.REPLACEMENTS_USED;
import static org.junit.jupiter.api.Assertions.*;

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

    private void compareReplacements(ReplacementUsed replacementUsedExpected, ReplacementUsed replacementUsedActual){
        assertEquals(replacementUsedExpected.getDiscount(), replacementUsedActual.getDiscount());
        assertEquals(replacementUsedExpected.getQuantity(), replacementUsedActual.getQuantity());
        assertEquals(replacementUsedExpected.getOwn(), replacementUsedActual.getOwn());
        assertEquals(replacementUsedExpected.getPrice(), replacementUsedActual.getPrice());
        assertEquals(replacementUsedExpected.getDiscount(), replacementUsedActual.getDiscount());
    }

    @Test
    void testCreateReplacementsOk() {
        BigDecimal priceReplacementA = BigDecimal.valueOf(20.00);
        BigDecimal priceReplacementB = BigDecimal.valueOf(20.00);

        ReplacementUsed[] replacementsUsed = {
                ReplacementUsed.builder()
                        .quantity(1).discount(10).own(true).price(priceReplacementA)
                        .replacement(Replacement.builder().reference("11111111").build())
                        .build(),
                ReplacementUsed.builder()
                        .quantity(2).discount(20).own(false).price(priceReplacementB)
                        .replacement(Replacement.builder().reference("33333333").build())
                        .build()
        };

        Revision revision = Revision.builder()
                .reference("rev-2")
                .replacementsUsed(List.of(replacementsUsed))
                .build();

        StepVerifier
                .create(this.revisionPersistence.createReplacementsUsed(revision))
                .expectNextMatches(revisionUpdated -> {
                    assertNotNull(revisionUpdated);
                    assertNotNull(revisionUpdated.getReference());
                    assertNotNull(revisionUpdated.getReplacementsUsed());

                    assertEquals(revision.getReference(), revisionUpdated.getReference());
                    assertEquals(revision.getReplacementsUsed().size(), revisionUpdated.getReplacementsUsed().size());

                    for(int i = 0 ; i < revision.getReplacementsUsed().size(); i++){
                        assertNotNull(revision.getReplacementsUsed().get(i).getReference());
                        compareReplacements(revision.getReplacementsUsed().get(i), revisionUpdated.getReplacementsUsed().get(i));
                    }
                    assertEquals(priceReplacementA.add(priceReplacementB), revisionUpdated.getCost());

                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testCreateReplacementsErrorByRevisionUnknown() {
        ReplacementUsed[] replacementsUsed = {
                ReplacementUsed.builder()
                        .quantity(1)
                        .discount(10)
                        .own(true)
                        .price(BigDecimal.valueOf(10.00))
                        .replacement(Replacement.builder().reference("11111111").build())
                        .build(),
                ReplacementUsed.builder()
                        .quantity(2)
                        .discount(20)
                        .own(false)
                        .price(BigDecimal.valueOf(20.00))
                        .replacement(Replacement.builder().reference("33333333").build())
                        .build()
        };

        Revision revision = Revision.builder()
                .reference("rev-unknown")
                .replacementsUsed(List.of(replacementsUsed))
                .build();

        StepVerifier
                .create(this.revisionPersistence.createReplacementsUsed(revision))
                .expectError()
                .verify();
    }

    @Test
    void testCreateReplacementsErrorByReplacementUnknown() {
        ReplacementUsed[] replacementsUsed = {
                ReplacementUsed.builder()
                        .quantity(1)
                        .discount(10)
                        .own(true)
                        .price(BigDecimal.valueOf(10.00))
                        .replacement(Replacement.builder().reference("11111111").build())
                        .build(),
                ReplacementUsed.builder()
                        .quantity(2)
                        .discount(20)
                        .own(false)
                        .price(BigDecimal.valueOf(20.00))
                        .replacement(Replacement.builder().reference("ref-unknown").build())
                        .build()
        };

        Revision revision = Revision.builder()
                .reference("rev-2")
                .replacementsUsed(List.of(replacementsUsed))
                .build();

        StepVerifier
                .create(this.revisionPersistence.createReplacementsUsed(revision))
                .expectError()
                .verify();
    }

    @Test
    void testFindByReferenceOk() {
        String revisionReference = "rev-1";

        StepVerifier
                .create(this.revisionPersistence.findByReference(revisionReference))
                .expectNextMatches(revision -> {
                    assertNotNull(revision);
                    assertNotNull(revision.getReference());

                    assertEquals(revisionReference, revision.getReference());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testFindByReferenceErrorByRevisionUnknown() {
        String revisionReference = "rev-unknown";

        StepVerifier
                .create(this.revisionPersistence.findByReference(revisionReference))
                .expectError()
                .verify();
    }

    @Test
    void testUpdateRevisionOk(){
        Technician technician = Technician.builder()
                .identificationId("11111111-T")
                .build();

        Revision revision = Revision.builder()
                .reference("rev-1")
                .diagnostic("REVISION UPDATE")
                .registerDate(LocalDateTime.now())
                .initialKilometers(1000)
                .workedHours(10)
                .technician(technician)
                .status(StatusRevision.POR_CONFIRMAR)
                .build();

        StepVerifier
                .create(this.revisionPersistence.update(revision))
                .expectNextMatches(revisionCreated -> {
                    assertNotNull(revisionCreated);
                    assertNotNull(revisionCreated.getReference());
                    if(!revision.isFinaliced()){
                        assertNull(revisionCreated.getDepartureDate());
                        assertNull(revisionCreated.getDepartureKilometers());
                    }
                    assertEquals(StatusRevision.POR_CONFIRMAR, revisionCreated.getStatus());
                    assertEquals(revisionCreated.getTechnicianIdentification(), revision.getTechnicianIdentification());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testUpdateRevisionErrorByReferenceUnknown(){
        Technician technician = Technician.builder()
                .identificationId("11111111-T")
                .build();

        Revision revision = Revision.builder()
                .reference("rev-unknown")
                .diagnostic("REVISION UPDATE")
                .registerDate(LocalDateTime.now())
                .initialKilometers(1000)
                .workedHours(10)
                .technician(technician)
                .status(StatusRevision.POR_CONFIRMAR)
                .build();

        StepVerifier
                .create(this.revisionPersistence.update(revision))
                .expectError()
                .verify();
    }


    @Test
    void testUpdateCostOk(){
        String revisionReference = "rev-4";

        StepVerifier
                .create(this.revisionPersistence.updateCostByReference(revisionReference))
                .expectNextMatches(revisionCreated -> {
                    assertNotNull(revisionCreated);
                    assertNotNull(revisionCreated.getReference());
                    assertNotNull(revisionCreated.getCost());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testDeleteLogicOk(){
        String revisionReference = "rev-4";

        StepVerifier
                .create(this.revisionPersistence.deleteLogic(revisionReference))
                .expectComplete()
                .verify();

        StepVerifier
                .create(this.revisionPersistence.findByReference(revisionReference))
                .expectNextMatches(revision -> {
                    assertNotNull(revision);
                    assertNotNull(revision.getReference());

                    assertNotNull(revision.getLeaveDate());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testDeleteLogicErrorByReferenceUnknown(){
        String revisionReference = "rev-unknown";

        StepVerifier
                .create(this.revisionPersistence.deleteLogic(revisionReference))
                .expectError()
                .verify();
    }
}
