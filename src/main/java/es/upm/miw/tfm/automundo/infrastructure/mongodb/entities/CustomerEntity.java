package es.upm.miw.tfm.automundo.infrastructure.mongodb.entities;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class CustomerEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String identificationId;
    private LocalDateTime registrationDate;
    private LocalDateTime lastVisitDate;
    private String phone;
    private String mobilePhone;
    private String address;
    private String email;
    private String name;
    private String surName;
    private String secondSurName;

    public Customer toCustomer() {
        Customer customer = new Customer();
        BeanUtils.copyProperties(this, customer);
        return customer;
    }
}
