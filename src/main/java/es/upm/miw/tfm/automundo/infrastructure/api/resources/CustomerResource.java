package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.model.CustomerCreationUpdate;
import es.upm.miw.tfm.automundo.domain.services.CustomerService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.CustomerLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(CustomerResource.CUSTOMERS)
public class CustomerResource {
    public static final String CUSTOMERS = "/customers";
    public static final String SEARCH = "/search";
    public static final String IDENTIFICATION_ID = "/{identification}";

    private CustomerService customerService;

    @Autowired
    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(SEARCH)
    public Flux<CustomerLineDto> findByIdentificationIdAndNameAndSurNameAndSecondSurNameNullSafe(
            @RequestParam(required = false) String identificationId, @RequestParam(required = false) String name,
            @RequestParam(required = false) String surName, @RequestParam(required = false) String secondSurName) {
        return this.customerService.findByIdentificationIdAndNameAndSurNameAndSecondSurNameNullSafe(
                identificationId, name, surName, secondSurName)
                .map(CustomerLineDto::new);
    }

    @GetMapping(IDENTIFICATION_ID)
    public Mono<Customer> read(@PathVariable String identification) {
        return this.customerService.read(identification);
    }

    @PostMapping(produces = {"application/json"})
    public Mono<Customer> create(@Valid @RequestBody CustomerCreationUpdate customerCreationUpdate) {
        customerCreationUpdate.doDefault();
        return this.customerService.create(customerCreationUpdate);
    }
}
