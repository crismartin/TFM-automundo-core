package es.upm.miw.tfm.automundo.infrastructure.mongodb.entities;

import es.upm.miw.tfm.automundo.domain.model.VehicleType;
import es.upm.miw.tfm.automundo.domain.model.VehicleTypeCreation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class VehicleTypeEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String reference;
    private String name;
    private String description;

    public VehicleTypeEntity(VehicleTypeCreation vehicleTypeCreation) {
        BeanUtils.copyProperties(vehicleTypeCreation, this);
    }

    public VehicleTypeEntity(VehicleType vehicleType) {
        BeanUtils.copyProperties(vehicleType, this);
    }

    public VehicleType toVehicleType() {
        VehicleType vehicleType = new VehicleType();
        BeanUtils.copyProperties(this, vehicleType);
        return vehicleType;
    }
}
