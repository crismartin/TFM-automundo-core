package es.upm.miw.tfm.automundo.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Replacement {
    @NotBlank
    private String id;
    @NotBlank
    private String reference;
    private String name;
    private BigDecimal price;
    private String description;
    private Boolean active;
}
