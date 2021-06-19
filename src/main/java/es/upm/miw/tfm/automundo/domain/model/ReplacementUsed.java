package es.upm.miw.tfm.automundo.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.ReplacementUsedDto;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReplacementUsed {
    private String reference;
    private Integer quantity;
    private Boolean own;
    private BigDecimal price;
    private Integer discount;
    private Replacement replacement;
    private String revisionReference;

    public String getReplacementReference(){
        return replacement != null ? replacement.getReference() : null;
    }

    public BigDecimal getTotalPrice() {
        BigDecimal result = BigDecimal.valueOf(0.00);
        if(price != null){
            result = price;
        }
        return result;
    }

    public ReplacementUsed(ReplacementUsedDto replacementUsedDto){
        BeanUtils.copyProperties(replacementUsedDto, this);
    }
}
