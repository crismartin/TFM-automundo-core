package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VehicleSearchDto {
    private String plate;
    private String bin;
    private String customerName;
    private String customerSurname;
    private String customerSecondSurname;
}
