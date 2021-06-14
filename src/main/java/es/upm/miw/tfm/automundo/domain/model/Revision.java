package es.upm.miw.tfm.automundo.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.tfm.automundo.infrastructure.enums.StatusRevision;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Revision {
    private String reference;
    private String diagnostic;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime registerDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureDate;
    private Technician technician;
    private BigDecimal cost;
    private StatusRevision status;
    private Vehicle vehicle;

    public String getVehicleReference() {
        return vehicle.getReference();
    }

    public String getTechnicianCompleteName(){
        return technician != null ? technician.getCompleteName() : null;
    }

    public String getStatusName(){
        return status.getName();
    }
}
