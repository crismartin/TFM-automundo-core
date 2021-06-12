package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import es.upm.miw.tfm.automundo.domain.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDto {
    private String identificationId;
    private String completeName;
    private String mobilePhone;
    private String address;

    public CustomerDto(Customer customer){
        BeanUtils.copyProperties(customer, this);
    }
}
