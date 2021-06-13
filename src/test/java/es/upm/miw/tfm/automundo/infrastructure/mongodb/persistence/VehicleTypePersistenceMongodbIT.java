package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.TestConfig;
import es.upm.miw.tfm.automundo.domain.persistence.VehicleTypePersistence;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestConfig
class VehicleTypePersistenceMongodbIT {

    @Autowired
    private VehicleTypePersistence vehicleTypePersistence;

    @Test
    void testFindVehiclesByIdCustomer() {

        StepVerifier
                .create(this.vehicleTypePersistence.findAllActive())
                .recordWith(ArrayList::new)
                .thenConsumeWhile(x -> true)
                .expectRecordedMatches(vehicleTypes ->
                            vehicleTypes.stream()
                                    .map(vehicleType -> vehicleType.getActive() == Boolean.FALSE ? vehicleType : null)
                                    .filter(vehicleType -> vehicleType != null)
                                    .count() == 0
                )
                .verifyComplete();
    }
}
