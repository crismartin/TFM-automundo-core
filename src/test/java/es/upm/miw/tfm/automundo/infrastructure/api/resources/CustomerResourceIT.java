package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.model.CustomerCreation;
import es.upm.miw.tfm.automundo.domain.model.CustomerUpdate;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.CustomerLineDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
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
    void testFindByBarcodeAndUpdate() {
        Customer customerFound = this.webTestClient
                .get()
                .uri(CUSTOMERS + IDENTIFICATION_ID, "11111111-A")
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .value(Assertions::assertNotNull)
                .value(customer -> assertEquals("11111111-A", customer.getIdentificationId()
                )).returnResult().getResponseBody();

        CustomerUpdate customerUpdate = new CustomerUpdate();
        BeanUtils.copyProperties(customerFound, customerUpdate);
        customerUpdate.setEmail("cliente.modificado@gmail.com");
        customerUpdate.setMobilePhone("630881234");
        customerUpdate.setAddress("C/ Modificada, 345, Madrid");

        this.webTestClient
                .put()
                .uri(CUSTOMERS + IDENTIFICATION_ID, "11111111-A")
                .body(Mono.just(customerUpdate), CustomerUpdate.class)
                .exchange()
                .expectStatus().isOk()
                .expectBody(Customer.class)
                .value(Assertions::assertNotNull)
                .value(updatedCustomer -> {
                    assertEquals(customerUpdate.getEmail(), updatedCustomer.getEmail());
                    assertEquals(customerUpdate.getMobilePhone(), updatedCustomer.getMobilePhone());
                    assertEquals(customerUpdate.getAddress(), updatedCustomer.getAddress());
                    assertEquals(customerFound.getIdentificationId(), updatedCustomer.getIdentificationId());
                    assertEquals(customerFound.getPhone(), updatedCustomer.getPhone());
                    assertEquals(customerFound.getName(), updatedCustomer.getName());
                    assertEquals(customerFound.getSurName(), updatedCustomer.getSurName());
                    assertEquals(customerFound.getSecondSurName(), updatedCustomer.getSecondSurName());
                    assertEquals(customerFound.getRegistrationDate(), updatedCustomer.getRegistrationDate());
                    assertEquals(customerFound.getLastVisitDate(), updatedCustomer.getLastVisitDate());
                });
    }

    @Test
    void testCreateAndDelete() {
        //TODO make test also for deleting vehicles from a customer
        CustomerCreation customerCreation = CustomerCreation.builder().identificationId("99999999-A")
                .phone("967811566").mobilePhone("654744344").address("C/ Nuevo 123, Leganés").email("nuevocliente@gmail.com")
                .name("Marcos").surName("Alvaredo").secondSurName("Pino").build();
        Customer customer = this.webTestClient
                .post()
                .uri(CUSTOMERS)
                .body(Mono.just(customerCreation), CustomerCreation.class)
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
                }).returnResult().getResponseBody();
        assertNotNull(customer);

        this.webTestClient
                .delete()
                .uri(CUSTOMERS + IDENTIFICATION_ID, "99999999-A")
                .exchange()
                .expectStatus().isOk();

        this.webTestClient
                .get()
                .uri(CUSTOMERS + IDENTIFICATION_ID, "99999999-A")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testDeleteNotFoundException() {
        this.webTestClient
                .delete()
                .uri(CUSTOMERS + IDENTIFICATION_ID, "$$$$$$$$-$")
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void testConflictIdentificationIdException() {
        CustomerCreation customerCreation = CustomerCreation.builder().identificationId("11111111-A")
                .phone("967811566").mobilePhone("654744344").address("C/ Nuevo 123, Leganés").email("nuevocliente@gmail.com")
                .name("Marcos").surName("Alvaredo").secondSurName("Pino").build();
        this.webTestClient
                .post()
                .uri(CUSTOMERS)
                .body(Mono.just(customerCreation), CustomerCreation.class)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.CONFLICT);
    }

}
