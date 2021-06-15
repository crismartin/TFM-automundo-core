package es.upm.miw.tfm.automundo.domain.model;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VehicleTypeUpdate {
    private String name;
    private String description;
    private Boolean active;
}
