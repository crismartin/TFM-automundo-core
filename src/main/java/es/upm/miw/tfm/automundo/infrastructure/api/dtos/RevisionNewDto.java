package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevisionNewDto {

    @NotBlank
    private String vehicleReference;
    @NotBlank
    private String diagnostic;
    private LocalDateTime registerDate;
    private Integer initialKilometers;
    private TechnicianLineDto technician;
    private Integer workedHours;
    private BigDecimal cost;
    private String workDescription;

    public String getTechnicianIdentification(){
        return technician != null ? technician.getIdentificationId() : null;
    }
}
