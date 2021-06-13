package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.TestConfig;
import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.model.Vehicle;
import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import es.upm.miw.tfm.automundo.domain.persistence.VehiclePersistence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class VehiclePersistenceMongodbIT {
    @Autowired
    private VehiclePersistence vehiclePersistence;

    private static final String IDENTIFICATION_CUSTOMER_CREATION = "33333333-A";
    private static final String REFERENCE_VEHICLE_TYPE = "11111111";
    private static final String REFERENCE_VEHICLE = "ref-2002";

    @Test
    void testFindVehiclesByIdCustomer() {
        String identificationCustomer = "22222222-A";

        StepVerifier
                .create(this.vehiclePersistence.findVehiclesByIdCustomer(identificationCustomer))
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
                .create(this.vehiclePersistence.findByReference(referenceVehicle))
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
                .model("MERCEDES BENZ CLASE A").yearRelease(2020).plate("EM-2020").bin("MB-CA")
                .reference("TEST-ref-1001")
                .vehicleType(vehicleType)
                .build();

        StepVerifier
                .create(this.vehiclePersistence.create(vehicleDummy))
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
                .model("MERCEDES BENZ CLASE A").yearRelease(2020).plate("EM-2020").bin("MB-CA")
                .reference("TEST-ref-1001")
                .vehicleType(vehicleType)
                .build();

        StepVerifier
                .create(this.vehiclePersistence.create(vehicleDummy))
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
                .model("MERCEDES BENZ CLASE A").yearRelease(2020).plate("EM-2020").bin("MB-CA")
                .reference("TEST-ref-1001")
                .vehicleType(vehicleType)
                .build();

        StepVerifier
                .create(this.vehiclePersistence.create(vehicleDummy))
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
                .model("MERCEDES BENZ CLASE A").yearRelease(2020).plate("EM-2020").bin("vh-1001")
                .reference("TEST-ref-1001")
                .vehicleType(vehicleType)
                .build();

        StepVerifier
                .create(this.vehiclePersistence.create(vehicleDummy))
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
                .model("TESTING MODEL").yearRelease(2030).plate("ED-202").bin("vh-500")
                .vehicleType(vehicleType)
                .build();

        StepVerifier
                .create(this.vehiclePersistence.update(vehicleDummy))
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
}
