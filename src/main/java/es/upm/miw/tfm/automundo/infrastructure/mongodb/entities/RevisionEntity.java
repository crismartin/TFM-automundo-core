package es.upm.miw.tfm.automundo.infrastructure.mongodb.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.upm.miw.tfm.automundo.domain.model.Revision;
import es.upm.miw.tfm.automundo.infrastructure.enums.StatusRevision;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class RevisionEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String reference;
    private String diagnostic;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime registerDate;
    private Integer initialKilometers;
    private Integer workedHours;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureDate;
    @DBRef(lazy = true)
    private TechnicianEntity technicianEntity;
    private BigDecimal cost;
    private StatusRevision status;
    @DBRef(lazy = true)
    private VehicleEntity vehicleEntity;

    public RevisionEntity(Revision revision){
        BeanUtils.copyProperties(revision, this);
        if(revision != null){
            this.technicianEntity = revision.getTechnician() != null ? new TechnicianEntity(revision.getTechnician()) : null;
            this.vehicleEntity = revision.getVehicle() != null ? new VehicleEntity(revision.getVehicle()) : null;
        }
    }

    public Revision toRevision(){
        Revision revision = new Revision();

        BeanUtils.copyProperties(this, revision);
        revision.setVehicle(this.getVehicleEntity().toVehicle());

        if(technicianEntity != null){
            revision.setTechnician(this.getTechnicianEntity().toTechnician());
        }
        return revision;
    }

    public String getVehicleReference(){
        return this.vehicleEntity.getReference();
    }

    public void setFieldsCreation() {
        reference = UUID.randomUUID().toString();
        status = StatusRevision.POR_CONFIRMAR;
    }
}
