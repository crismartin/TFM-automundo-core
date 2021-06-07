package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.infrastructure.api.RestClientTestService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.CustomerLineDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.reactive.server.WebTestClient;


import static org.junit.jupiter.api.Assertions.assertTrue;
import static es.upm.miw.tfm.automundo.infrastructure.api.resources.CustomerResource.*;

@RestTestConfig
public class CustomerResourceIT {

    @Autowired
    private WebTestClient webTestClient;
    //@Autowired
    //private RestClientTestService restClientTestService;

    @Test
    void findByIdentificationIdAndNameAndSurNameAndSecondSurNameNullSafe() {
        this.webTestClient
                .get()
                .uri(uriBuilder -> uriBuilder
                        .path(CUSTOMERS + SEARCH)
                        .queryParam("surName", "García")
                        .build())
                .exchange()
                .expectStatus().isOk()
                .expectBodyList(CustomerLineDto.class)
                .value(Assertions::assertNotNull)
                .value(customerLines -> assertTrue(customerLines
                        .stream().allMatch(customerLine -> customerLine.getCompleteName().toLowerCase().contains("garcía"))));
    }
}
