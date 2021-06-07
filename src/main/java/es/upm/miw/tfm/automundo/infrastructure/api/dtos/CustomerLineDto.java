package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Getter
public class CustomerLineDto {
    private String identificationId;
    private String completeName;
    private String mobilePhone;
    private String address;

    public CustomerLineDto(Customer customer) {
        this.identificationId = customer.getIdentificationId();
        this.completeName = customer.getName() + " " + customer.getSurName() + " " + customer.getSecondSurName();
        this.mobilePhone = customer.getMobilePhone();
        this.address = customer.getAddress();
    }
}
