package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class VehicleTypeDto {
    private String id;
    private String reference;
    private String name;

    public VehicleTypeDto(VehicleType vehicleType){
        BeanUtils.copyProperties(vehicleType, this);
    }
}
