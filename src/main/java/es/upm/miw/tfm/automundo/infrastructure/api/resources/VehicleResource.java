package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.services.VehicleService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleDto;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(VehicleResource.VEHICLES)
public class VehicleResource {
    public static final String VEHICLES = "/vehicles";
    public static final String REFERENCE = "/{reference}";
    public static final String CUSTOMERS_IDENTIFICATION = "/customer/{identification}";


    private VehicleService vehicleService;

    @Autowired
    public VehicleResource(VehicleService vehicleService){
        this.vehicleService = vehicleService;
    }

    @GetMapping(CUSTOMERS_IDENTIFICATION)
    public Flux<VehicleLineDto> findVehiclesByIdCustomer(@PathVariable String identification) {
        return this.vehicleService.findVehiclesByIdCustomer(identification)
                .map(VehicleLineDto::new);
    }

    @GetMapping(REFERENCE)
    public Mono<VehicleDto> findByReference(@PathVariable String reference) {
        return this.vehicleService.findByReference(reference)
                .map(VehicleDto::new);
    }

}
