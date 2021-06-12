package es.upm.miw.tfm.automundo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleNewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Vehicle {
    private String id;
    private String bin;
    private String reference;
    private String plate;
    private String model;
    @Positive
    private Integer yearRelease;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime registerDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastViewDate;
    private VehicleType vehicleType;
    private String typeNumber;
    private Customer customer;

    public String getIdentificationCustomer(){
        return this.customer != null ? customer.getIdentificationId() : null;
    }

    public String getVehicleTypeReference(){
        return vehicleType != null ? vehicleType.getReference() : null;
    }

    public Vehicle(VehicleNewDto vehicleNew){
        BeanUtils.copyProperties(vehicleNew, this);
        this.customer = Customer.builder()
                .identificationId(vehicleNew.getIdentificationCustomer())
                .build();
        this.vehicleType = VehicleType.builder()
                .reference(vehicleNew.getVehicleTypeReference())
                .build();
    }
}
