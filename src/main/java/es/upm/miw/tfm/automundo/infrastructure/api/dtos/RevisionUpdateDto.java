package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import es.upm.miw.tfm.automundo.domain.model.Technician;
import es.upm.miw.tfm.automundo.infrastructure.enums.StatusRevision;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RevisionUpdateDto {
    @NotBlank
    @NotNull
    private String reference;
    private String diagnostic;
    private LocalDateTime registerDate;
    private Integer initialKilometers;
    private Integer workedHours;
    private String workDescription;
    private LocalDateTime departureDate;
    private Integer departureKilometers;
    private Technician technician;
    private BigDecimal cost;
    @NotNull
    private StatusRevision status;

    public String getTechnicianIdentification() {
        return technician != null ? technician.getIdentificationId() : null;
    }
}
