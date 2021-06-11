package es.upm.miw.tfm.automundo.domain.model;

import lombok.*;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class ReplacementUpdate {
    private String name;
    private BigDecimal price;
    private String description;
}
