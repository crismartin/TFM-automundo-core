package es.upm.miw.tfm.automundo.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@Builder
@AllArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VehicleType {
    @NotBlank
    private String id;
    @NotBlank
    private String reference;
    private String name;
    private String description;
    private Boolean active;
}
