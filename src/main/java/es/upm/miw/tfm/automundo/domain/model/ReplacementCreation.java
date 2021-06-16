package es.upm.miw.tfm.automundo.domain.model;

import lombok.*;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ReplacementCreation {
    @NotBlank
    private String reference;
    private String name;
    private BigDecimal price;
    private String description;
}
