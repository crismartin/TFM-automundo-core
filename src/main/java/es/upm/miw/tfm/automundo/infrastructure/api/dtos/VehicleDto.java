package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.upm.miw.tfm.automundo.domain.model.Vehicle;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class VehicleDto {
    private String id;
    private String reference;
    private String bin;
    private String plate;
    private String model;
    private Integer yearRelease;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime registerDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastViewDate;
    private VehicleTypeDto vehicleType;
    private String ownerNumber;

    public VehicleDto(Vehicle vehicle) {
        BeanUtils.copyProperties(vehicle, this);
        if(vehicle.getVehicleType() != null){
            vehicleType = new VehicleTypeDto(vehicle.getVehicleType());
        }
    }
}
