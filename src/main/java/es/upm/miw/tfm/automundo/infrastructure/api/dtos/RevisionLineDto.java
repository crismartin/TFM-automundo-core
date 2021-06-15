package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import es.upm.miw.tfm.automundo.domain.model.Revision;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevisionLineDto {

    private String reference;
    private String diagnostic;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime registerDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime departureDate;
    private String technicianName;
    private BigDecimal cost;
    private String statusName;

    public RevisionLineDto(Revision revision){
        BeanUtils.copyProperties(revision, this);
        if(revision != null){
            if(revision.getTechnician() != null){
                technicianName = revision.getTechnicianCompleteName();
            }
            statusName = revision.getStatusName();
        }
    }

}
