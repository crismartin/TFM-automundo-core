package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import es.upm.miw.tfm.automundo.domain.services.VehicleTypeService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleTypeLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(VehicleTypeResource.VEHICLE_TYPES)
public class VehicleTypeResource {
    public static final String VEHICLE_TYPES = "/vehicle-types";
    public static final String SEARCH = "/search";
    public static final String REFERENCE = "/{reference}";

    private VehicleTypeService vehicleTypeService;

    @Autowired
    public VehicleTypeResource(VehicleTypeService vehicleTypeService) {
        this.vehicleTypeService = vehicleTypeService;
    }

    @GetMapping(SEARCH)
    public Flux<VehicleTypeLineDto> findByReferenceAndNameAndDescriptionNullSafe(
            @RequestParam(required = false) String reference, @RequestParam(required = false) String name,
            @RequestParam(required = false) String description) {
        return this.vehicleTypeService.findByReferenceAndNameAndDescriptionNullSafe(reference, name, description)
                .map(VehicleTypeLineDto::new);
    }

    @GetMapping(REFERENCE)
    public Mono<VehicleType> read(@PathVariable String reference) {
        return this.vehicleTypeService.read(reference);
    }
}
