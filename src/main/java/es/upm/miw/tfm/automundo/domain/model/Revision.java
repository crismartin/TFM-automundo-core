package es.upm.miw.tfm.automundo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.RevisionNewDto;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.RevisionUpdateDto;
import es.upm.miw.tfm.automundo.infrastructure.enums.StatusRevision;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Revision {
    private String reference;
    private String diagnostic;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime registerDate;
    private Integer initialKilometers;
    private Integer workedHours;
    private String workDescription;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureDate;
    private Integer departureKilometers;
    private Technician technician;
    private BigDecimal cost;
    private StatusRevision status;
    private Vehicle vehicle;
    private List<ReplacementUsed> replacementsUsed;

    public Revision(RevisionNewDto revisionNewDto){
        BeanUtils.copyProperties(revisionNewDto, this);
        if(revisionNewDto.getTechnician() != null){
            technician = Technician.builder()
                    .identificationId(revisionNewDto.getTechnicianIdentification())
                    .build();
        }
        if(revisionNewDto.getVehicleReference() != null){
            vehicle = Vehicle.builder()
                    .reference(revisionNewDto.getVehicleReference())
                    .build();
        }
    }

    public Revision(RevisionUpdateDto revisionUpdateDto) {
        BeanUtils.copyProperties(revisionUpdateDto, this);
        if(revisionUpdateDto.getTechnician() != null){
            BeanUtils.copyProperties(revisionUpdateDto.getTechnician(), this.technician);
        }
    }

    public String getVehicleReference() {
        return vehicle != null ? vehicle.getReference() : null;
    }

    public String getTechnicianCompleteName(){
        return technician != null ? technician.getCompleteName() : null;
    }

    public String getTechnicianIdentification(){
        return technician != null ? technician.getIdentificationId() : null;
    }

    public String getStatusName(){
        return status != null ? status.getName() : null;
    }

    public Boolean isFinaliced(){
        return status == StatusRevision.FINALIZADO;
    }

    public void calTotalCost() {
        if(replacementsUsed != null && !replacementsUsed.isEmpty()){
            BigDecimal totalCost = replacementsUsed.stream()
                    .map(ReplacementUsed::getTotalPrice)
                    .reduce(BigDecimal.valueOf(0.00), (bigDecimal, bigDecimal2) -> bigDecimal.add(bigDecimal2));
            setCost(totalCost);
        }
    }

    public BigDecimal getBaseTax() {
        BigDecimal result = BigDecimal.valueOf(0.00);
        if(cost != null){
            result = result.add(cost).multiply((BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(21).divide(BigDecimal.valueOf(100)))));
        }
        return result;
    }

    public BigDecimal getTaxValue() {
        BigDecimal result = BigDecimal.valueOf(0.00);
        if(cost != null && !result.equals(cost)){
            result = cost.subtract(getBaseTax());
        }
        return result;
    }
}
