package es.upm.miw.tfm.automundo.domain.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReplacementCreation {
    @NotBlank
    private String reference;
    private String name;
    private BigDecimal price;
    private String description;
}
