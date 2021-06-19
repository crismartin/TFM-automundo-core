package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.domain.model.ReplacementUsed;
import es.upm.miw.tfm.automundo.domain.persistence.ReplacementUsedPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ReplacementUsedService {

    private ReplacementUsedPersistence replacementUsedPersistence;

    @Autowired
    public ReplacementUsedService(ReplacementUsedPersistence replacementUsedPersistence){
        this.replacementUsedPersistence = replacementUsedPersistence;
    }

    public Mono<ReplacementUsed> update(ReplacementUsed replacementUsed){
        return replacementUsedPersistence.update(replacementUsed);
    }

}
