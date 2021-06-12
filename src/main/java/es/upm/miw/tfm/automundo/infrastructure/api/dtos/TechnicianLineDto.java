package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.upm.miw.tfm.automundo.domain.model.Technician;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Getter
public class TechnicianLineDto {
    private String identificationId;
    private String completeName;
    private String ssNumber;
    private String mobile;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime registrationDate;
    @JsonFormat(pattern = "dd-MM-yyyy")
    private LocalDateTime leaveDate;
    private Boolean active;

    public TechnicianLineDto(Technician technician) {
        BeanUtils.copyProperties(technician, this);
        this.completeName = technician.getName() + " " + technician.getSurName() + " " + technician.getSecondSurName();
    }
}
