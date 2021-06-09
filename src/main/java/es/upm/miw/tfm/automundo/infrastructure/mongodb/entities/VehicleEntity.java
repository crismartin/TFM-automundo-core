package es.upm.miw.tfm.automundo.infrastructure.mongodb.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.upm.miw.tfm.automundo.domain.model.Customer;
import es.upm.miw.tfm.automundo.domain.model.Vehicle;
import es.upm.miw.tfm.automundo.domain.model.OwnerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class VehicleEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String bin;
    private String plate;
    private String model;
    private Integer yearRelease;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime registerDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime lastViewDate;
    @DBRef(lazy = true)
    private OwnerTypeEntity ownerType;
    private String ownerNumber;
    @DBRef(lazy = true)
    private CustomerEntity customer;

    public Vehicle toVehicle(){
        Vehicle vehicle = new Vehicle();
        BeanUtils.copyProperties(this, vehicle);

        vehicle.setCustomer(getCustomerFromEntity());
        vehicle.setOwnerType(getOwnerTypeFromEntity());

        return vehicle;
    }

    private Customer getCustomerFromEntity(){
        return (this.customer != null) ? this.customer.toCustomer() : null;
    }

    private OwnerType getOwnerTypeFromEntity(){
        return (this.ownerType != null ) ? this.ownerType.toOwnerType() : null;
    }

    public String getIdCustomer(){
        return this.customer != null ? this.customer.getId() : null;
    }
}
