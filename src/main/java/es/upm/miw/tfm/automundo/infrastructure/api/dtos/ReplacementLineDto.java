package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import es.upm.miw.tfm.automundo.domain.model.Replacement;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Getter
public class ReplacementLineDto {
    private String reference;
    private String name;
    private BigDecimal price;
    private String description;
    private Boolean active;

    public ReplacementLineDto(Replacement replacement) {
        BeanUtils.copyProperties(replacement, this);
    }
}
