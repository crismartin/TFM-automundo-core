package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.model.CustomerCreationUpdate;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.CustomerLineDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static es.upm.miw.tfm.automundo.infrastructure.api.resources.CustomerResource.*;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void testFindByBarcode() {
        this.webTestClient
                .get()
                .uri(CUSTOMERS + IDENTIFICATION_ID, "11111111-A")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .value(Assertions::assertNotNull)
                .value(customer -> assertEquals("11111111-A", customer.getIdentificationId()
                ));
    }

    @Test
    void testCreate() {
        CustomerCreationUpdate customerCreation = CustomerCreationUpdate.builder().identificationId("99999999-A")
                .phone("967811566").mobilePhone("654744344").address("C/ Nuevo 123, Leganés").email("nuevocliente@gmail.com")
                .name("Marcos").surName("Alvaredo").secondSurName("Pino").build();
        this.webTestClient
                .post()
                .uri(CUSTOMERS)
                .body(Mono.just(customerCreation), CustomerCreationUpdate.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .value(Assertions::assertNotNull)
                .value(customerCreated -> {
                    assertNotNull(customerCreated.getId());
                    assertEquals("99999999-A", customerCreated.getIdentificationId());
                    assertEquals("967811566", customerCreated.getPhone());
                    assertEquals("654744344", customerCreated.getMobilePhone());
                    assertEquals("C/ Nuevo 123, Leganés", customerCreated.getAddress());
                    assertEquals("nuevocliente@gmail.com", customerCreated.getEmail());
                    assertEquals("Marcos", customerCreated.getName());
                    assertEquals("Alvaredo", customerCreated.getSurName());
                    assertEquals("Pino", customerCreated.getSecondSurName());
                    assertNotNull(customerCreated.getRegistrationDate());
                    assertNotNull(customerCreated.getLastVisitDate());
                });
    }

    @Test
    void testConflictIdentificationIdException() {
        CustomerCreationUpdate customerCreation = CustomerCreationUpdate.builder().identificationId("11111111-A")
                .phone("967811566").mobilePhone("654744344").address("C/ Nuevo 123, Leganés").email("nuevocliente@gmail.com")
                .name("Marcos").surName("Alvaredo").secondSurName("Pino").build();
        this.webTestClient
                .post()
                .uri(CUSTOMERS)
                .body(Mono.just(customerCreation), CustomerCreationUpdate.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }
}
