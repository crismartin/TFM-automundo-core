package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.services.CustomerService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.CustomerLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(CustomerResource.CUSTOMERS)
public class CustomerResource {
    public static final String CUSTOMERS = "/customers";
    public static final String SEARCH = "/search";

    private CustomerService customerService;

    @Autowired
    public CustomerResource(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(SEARCH)
    public Flux<CustomerLineDto> findByIdentificationIdAndNameAndSurNameAndSecondSurNameNullSafe(
            @RequestParam(required = false) String identificationId, @RequestParam(required = false) String name,
            @RequestParam(required = false) String surName, @RequestParam(required = false) String secondSurName) {
        return this.customerService.findByIdentificationidAndNameAndSurnameAndSecondsurnameNullSafe(
                identificationId, name, surName, secondSurName)
                .map(CustomerLineDto::new);
    }
}