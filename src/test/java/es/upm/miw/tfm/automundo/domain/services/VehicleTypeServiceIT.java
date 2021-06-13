package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.TestConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.test.StepVerifier;

import java.util.ArrayList;

@TestConfig
class VehicleTypeServiceIT {

    @Autowired
    private VehicleTypeService vehicleTypeService;

    @Test
    void testFindAllActive() {

        StepVerifier
                .create(this.vehicleTypeService.findAllActive())
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
