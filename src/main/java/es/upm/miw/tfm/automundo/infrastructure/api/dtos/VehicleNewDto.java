package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleNewDto {
    @NotBlank
    private String plate;
    @NotBlank
    private String bin;
    @NotBlank
    private String model;
    private Integer yearRelease;
    private VehicleTypeDto vehicleType;
    private String typeNumber;
    @NotBlank
    private String identificationCustomer;

    public String getVehicleTypeReference(){
        return this.vehicleType != null ? this.vehicleType.getReference() : null;
    }
}
