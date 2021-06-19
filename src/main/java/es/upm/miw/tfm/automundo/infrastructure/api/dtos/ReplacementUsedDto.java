package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import es.upm.miw.tfm.automundo.domain.model.Replacement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReplacementUsedDto {
    private String reference;
    private Integer quantity;
    private Boolean own;
    private BigDecimal price;
    private Integer discount;
    private Replacement replacement;
    private String revisionReference;
}
