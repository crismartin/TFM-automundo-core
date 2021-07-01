package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import es.upm.miw.tfm.automundo.domain.model.Vehicle;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class VehicleLineCustomerDto {
    private String reference;
    private String plate;
    private String bin;
    private String model;
    private Integer yearRelease;
    private String customer;

    public VehicleLineCustomerDto(Vehicle vehicle) {
        BeanUtils.copyProperties(vehicle, this);
        customer = vehicle.getCustomerNameComplete();
    }
}
