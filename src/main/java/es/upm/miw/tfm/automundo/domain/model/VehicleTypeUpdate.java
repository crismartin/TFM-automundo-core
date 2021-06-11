package es.upm.miw.tfm.automundo.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class VehicleTypeUpdate {
    private String name;
    private String description;
}
