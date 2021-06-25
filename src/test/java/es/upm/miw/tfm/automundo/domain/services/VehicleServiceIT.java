package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.TestConfig;
import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.model.Vehicle;
import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class VehicleServiceIT {

    private static final String IDENTIFICATION_CUSTOMER_CREATION = "33333333-A";
    private static final String REFERENCE_VEHICLE_TYPE = "11111111";
    private static final String REFERENCE_VEHICLE = "ref-2002";

    @Autowired
    private VehicleService vehicleService;

    @Test
    void testFindVehiclesByIdCustomer() {
        String identificationCustomer = "22222222-A";

        StepVerifier
                .create(this.vehicleService.findVehiclesByIdCustomer(identificationCustomer))
                .expectNextMatches(vehicle -> {
                    assertNotNull(vehicle);
                    assertEquals(vehicle.getIdentificationCustomer(), identificationCustomer);
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testFindByReference(){
        String referenceVehicle = "ref-1001";
        StepVerifier
                .create(this.vehicleService.findByReference(referenceVehicle))
                .expectNextMatches(vehicle -> {
                    assertNotNull(vehicle);
                    assertEquals(vehicle.getReference(), referenceVehicle);
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testCreateOk(){
        Customer customer = Customer.builder()
                .identificationId(IDENTIFICATION_CUSTOMER_CREATION)
                .build();

        VehicleType vehicleType = VehicleType.builder()
                .reference(REFERENCE_VEHICLE_TYPE)
                .build();

        Vehicle vehicleDummy = Vehicle.builder().customer(customer).registerDate(LocalDateTime.now()).lastViewDate(LocalDateTime.now())
                .model("MERCEDES BENZ CLASE A").yearRelease(2020).plate("SM-2020").bin("SB-CS")
                .reference("TEST-ref-1001")
                .vehicleType(vehicleType)
                .build();

        StepVerifier
                .create(this.vehicleService.create(vehicleDummy))
                .expectNextMatches(vehicle -> {
                    assertNotNull(vehicle);
                    assertNotNull(vehicle.getReference());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testCreateErrorByVehicleTypeUnknow(){
        Customer customer = Customer.builder()
                .identificationId(IDENTIFICATION_CUSTOMER_CREATION)
                .build();

        VehicleType vehicleType = VehicleType.builder()
                .reference("1")
                .build();

        Vehicle vehicleDummy = Vehicle.builder().customer(customer).registerDate(LocalDateTime.now()).lastViewDate(LocalDateTime.now())
                .model("MERCEDES BENZ CLASE A").yearRelease(2020).plate("SM-2020").bin("SB-CS")
                .reference("TEST-ref-1001")
                .vehicleType(vehicleType)
                .build();

        StepVerifier
                .create(this.vehicleService.create(vehicleDummy))
                .expectError()
                .verify();
    }

    @Test
    void testCreateErrorByCustomerUnknow(){
        Customer customer = Customer.builder()
                .identificationId("dummy_customer")
                .build();

        VehicleType vehicleType = VehicleType.builder()
                .reference(REFERENCE_VEHICLE_TYPE)
                .build();

        Vehicle vehicleDummy = Vehicle.builder().customer(customer).registerDate(LocalDateTime.now()).lastViewDate(LocalDateTime.now())
                .model("MERCEDES BENZ CLASE A").yearRelease(2020).plate("SM-2020").bin("SB-CS")
                .reference("TEST-ref-1001")
                .vehicleType(vehicleType)
                .build();

        StepVerifier
                .create(this.vehicleService.create(vehicleDummy))
                .expectError()
                .verify();
    }

    @Test
    void testCreateErrorByBinAlreadyExist(){
        Customer customer = Customer.builder()
                .identificationId(IDENTIFICATION_CUSTOMER_CREATION)
                .build();

        VehicleType vehicleType = VehicleType.builder()
                .reference(REFERENCE_VEHICLE_TYPE)
                .build();

        Vehicle vehicleDummy = Vehicle.builder().customer(customer).registerDate(LocalDateTime.now()).lastViewDate(LocalDateTime.now())
                .model("MERCEDES BENZ CLASE A").yearRelease(2020).plate("EM-2020").bin("vh-100")
                .reference("TEST-ref-1001")
                .vehicleType(vehicleType)
                .build();

        StepVerifier
                .create(this.vehicleService.create(vehicleDummy))
                .expectError()
                .verify();
    }

    @Test
    void testUpdateOk(){
        Customer customer = Customer.builder()
                .identificationId(IDENTIFICATION_CUSTOMER_CREATION)
                .build();

        VehicleType vehicleType = VehicleType.builder()
                .reference(REFERENCE_VEHICLE_TYPE)
                .build();

        Vehicle vehicleDummy = Vehicle.builder()
                .reference(REFERENCE_VEHICLE)
                .customer(customer)
                .model("TESTING MODEL").yearRelease(2030).plate("ED-202").bin("vh-911")
                .vehicleType(vehicleType)
                .build();

        StepVerifier
                .create(this.vehicleService.update(vehicleDummy))
                .expectNextMatches(vehicle -> {
                    assertNotNull(vehicle);
                    assertNotNull(vehicle.getRegisterDate());
                    assertNotNull(vehicle.getLastViewDate());

                    assertEquals(vehicleDummy.getIdentificationCustomer(), vehicle.getIdentificationCustomer());
                    assertEquals(vehicleDummy.getReference(), vehicle.getReference());
                    assertEquals(vehicleDummy.getBin(), vehicle.getBin());
                    assertEquals(vehicleDummy.getPlate(), vehicle.getPlate());
                    assertEquals(vehicleDummy.getVehicleTypeReference(), vehicle.getVehicleTypeReference());
                    assertEquals(vehicleDummy.getYearRelease(), vehicle.getYearRelease());
                    assertEquals(vehicleDummy.getModel(), vehicle.getModel());

                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testDeleteLogicOk(){
        String referenceVehicle = "ref-2004";
        StepVerifier
                .create(this.vehicleService.deleteLogic(referenceVehicle))
                .expectComplete()
                .verify();

        StepVerifier
                .create(this.vehicleService.findByReference(referenceVehicle))
                .expectNextMatches(vehicle -> {
                    assertNotNull(vehicle);
                    assertNotNull(vehicle.getLeaveDate());
                    return true;
                })
                .thenCancel()
                .verify();
    }

    @Test
    void testDeleteLogicErrorByReferenceUnknown(){
        String referenceVehicle = "ref-unknown";
        StepVerifier
                .create(this.vehicleService.deleteLogic(referenceVehicle))
                .expectError()
                .verify();
    }
}
