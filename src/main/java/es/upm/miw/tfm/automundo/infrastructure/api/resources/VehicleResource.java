package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.model.Vehicle;
import es.upm.miw.tfm.automundo.domain.services.VehicleService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@SecurityRequirement(name = "bearerAuth")
@RequestMapping(VehicleResource.VEHICLES)
public class VehicleResource {
    public static final String VEHICLES = "/vehicles";
    public static final String REFERENCE = "/{reference}";
    public static final String CUSTOMERS_IDENTIFICATION = "/customer/{identification}";
    public static final String SEARCH = "/search";

    private VehicleService vehicleService;

    @Autowired
    public VehicleResource(VehicleService vehicleService){
        this.vehicleService = vehicleService;
    }

    private String trimParam(String param){
        return param != null ? param.trim() : null;
    }

    @GetMapping(SEARCH)
    public Flux<VehicleLineCustomerDto> findByPlateAndBinAndCustomerNullSafe(@RequestParam(required = false) String plate,
                                                                     @RequestParam(required = false) String bin,
                                                                     @RequestParam(required = false) String name,
                                                                     @RequestParam(required = false) String surName,
                                                                     @RequestParam(required = false) String secondSurname) {
        Vehicle filterParams = Vehicle.builder()
                .plate(trimParam(plate))
                .bin(trimParam(bin))
                .customer(Customer.builder()
                        .name(trimParam(name))
                        .surName(trimParam(surName))
                        .secondSurName(trimParam(secondSurname))
                        .build())
                .build();
        return this.vehicleService.findByPlateAndBinAndCustomerNullSafe(filterParams)
                .map(VehicleLineCustomerDto::new);
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

    @PostMapping(produces = {"application/json"})
    public Mono<VehicleDto> create(@Valid @RequestBody VehicleNewDto vehicle){
        return this.vehicleService.create(new Vehicle(vehicle))
                .map(VehicleDto::new);
    }

    @PutMapping(REFERENCE)
    public Mono<VehicleDto> update(@Valid @RequestBody VehicleNewDto vehicleUpdate, @PathVariable String reference){
        Vehicle vehicle = new Vehicle(vehicleUpdate);
        vehicle.setReference(reference);
        return this.vehicleService.update(vehicle)
                .map(VehicleDto::new);
    }

    @DeleteMapping(REFERENCE)
    public Mono<Void> deleteLogic(@PathVariable String reference){
        return this.vehicleService.deleteLogic(reference);
    }
}
