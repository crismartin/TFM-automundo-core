package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.services.VehicleTypeService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleTypeLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(VehicleTypeResource.VEHICLE_TYPES)
public class VehicleTypeResource {
    public static final String VEHICLE_TYPES = "/vehicle-types";
    public static final String SEARCH = "/search";

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
}
