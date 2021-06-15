package es.upm.miw.tfm.automundo.domain.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class VehicleTypeCreation {
    @NotBlank
    private String reference;
    private String name;
    private String description;
}
