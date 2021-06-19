package es.upm.miw.tfm.automundo.infrastructure.mongodb.entities;

import es.upm.miw.tfm.automundo.domain.model.ReplacementUsed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Document
public class ReplacementUsedEntity {
    @Id
    private String id;
    @Indexed(unique = true)
    private String reference;
    private Integer quantity;
    private Boolean own;
    private BigDecimal price;
    private Integer discount;
    @DBRef
    private ReplacementEntity replacementEntity;
    @DBRef
    private RevisionEntity revisionEntity;

    public ReplacementUsedEntity(ReplacementUsed replacementUsed) {
        BeanUtils.copyProperties(replacementUsed, this);
        replacementEntity = new ReplacementEntity(replacementUsed.getReplacement());
    }

    public ReplacementUsed toReplacementUsed(){
        ReplacementUsed replacementUsed = new ReplacementUsed();
        BeanUtils.copyProperties(this, replacementUsed);

        replacementUsed.setReplacement(replacementEntity.toReplacement());

        if(this.revisionEntity != null){
            replacementUsed.setRevisionReference(revisionEntity.getReference());
        }
        return replacementUsed;
    }
}
