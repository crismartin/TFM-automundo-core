package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import es.upm.miw.tfm.automundo.domain.model.VehicleTypeCreation;
import es.upm.miw.tfm.automundo.domain.model.VehicleTypeUpdate;
import es.upm.miw.tfm.automundo.domain.services.VehicleTypeService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleTypeDto;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleTypeLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

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
    public Flux<VehicleTypeLineDto> findByReferenceAndNameAndDescriptionAndActiveNullSafe(
            @RequestParam(required = false) String reference, @RequestParam(required = false) String name,
            @RequestParam(required = false) String description, @RequestParam(required = false) Boolean active) {
        return this.vehicleTypeService.findByReferenceAndNameAndDescriptionAndActiveNullSafe(reference, name, description, active)
                .map(VehicleTypeLineDto::new);
    }

    @GetMapping(REFERENCE)
    public Mono<VehicleType> read(@PathVariable String reference) {
        return this.vehicleTypeService.read(reference);
    }

    @PostMapping(produces = {"application/json"})
    public Mono<VehicleType> create(@Valid @RequestBody VehicleTypeCreation vehicleTypeCreation) {
        return this.vehicleTypeService.create(vehicleTypeCreation);
    }

    @PutMapping(REFERENCE)
    public Mono<VehicleType> update(@PathVariable String reference, @Valid @RequestBody VehicleTypeUpdate vehicleTypeUpdate) {
        return this.vehicleTypeService.update(reference, vehicleTypeUpdate);
    }

    @GetMapping()
    public Flux<VehicleTypeDto> findAllActive(){
        return this.vehicleTypeService.findAllActive()
                .map(VehicleTypeDto::new);
    }
}
