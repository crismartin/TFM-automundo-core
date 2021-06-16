package es.upm.miw.tfm.automundo.domain.model;

import lombok.*;

import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
public class ReplacementUpdate {
    private String name;
    private BigDecimal price;
    private String description;
    private Boolean active;
}
