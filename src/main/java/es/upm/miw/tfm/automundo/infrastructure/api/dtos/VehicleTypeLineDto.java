package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
@Getter
public class VehicleTypeLineDto {
    private String reference;
    private String name;
    private String description;
    private Boolean active;

    public VehicleTypeLineDto(VehicleType vehicleType) {
        BeanUtils.copyProperties(vehicleType, this);
    }
}
