package es.upm.miw.tfm.automundo.infrastructure.mongodb.entities;

import es.upm.miw.tfm.automundo.domain.model.OwnerType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class OwnerTypeEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String referenceId;
    private String name;

    public OwnerType toOwnerType(){
        OwnerType ownerType = new OwnerType();
        BeanUtils.copyProperties(this, ownerType);
        return ownerType;
    }
}
