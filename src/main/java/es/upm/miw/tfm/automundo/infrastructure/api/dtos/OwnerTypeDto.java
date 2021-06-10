package es.upm.miw.tfm.automundo.infrastructure.api.dtos;

import es.upm.miw.tfm.automundo.domain.model.OwnerType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

@Data
@NoArgsConstructor
public class OwnerTypeDto {
    private String id;
    private String reference;
    private String name;

    public OwnerTypeDto(OwnerType ownerType){
        BeanUtils.copyProperties(ownerType, this);
    }
}
