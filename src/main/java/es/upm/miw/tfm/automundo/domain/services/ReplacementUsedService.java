package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.domain.model.ReplacementUsed;
import es.upm.miw.tfm.automundo.domain.persistence.ReplacementUsedPersistence;
import es.upm.miw.tfm.automundo.domain.persistence.RevisionPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReplacementUsedService {

    private ReplacementUsedPersistence replacementUsedPersistence;
    private RevisionPersistence revisionPersistence;

    @Autowired
    public ReplacementUsedService(ReplacementUsedPersistence replacementUsedPersistence, RevisionPersistence revisionPersistence){
        this.replacementUsedPersistence = replacementUsedPersistence;
        this.revisionPersistence = revisionPersistence;
    }

    public Mono<ReplacementUsed> update(ReplacementUsed replacementUsed){
        return replacementUsedPersistence.update(replacementUsed)
                .flatMap(replacementUsed1 -> this.revisionPersistence.updateCostByReference(replacementUsed1.getRevisionReference())
                        .map(revision -> replacementUsed1));
    }

    public Mono<ReplacementUsed> create(ReplacementUsed replacementUsed) {
        return replacementUsedPersistence.create(replacementUsed)
                .flatMap(replacementUsed1 -> this.revisionPersistence.updateCostByReference(replacementUsed1.getRevisionReference())
                        .map(revision -> replacementUsed1));
    }

    public Flux<ReplacementUsed> findAllByRevisionReference(String revisionReference) {
        return replacementUsedPersistence.findAllByRevisionReference(revisionReference);
    }

    public Mono<Void> delete(String reference) {
        return replacementUsedPersistence.delete(reference)
                .flatMap(revisionReference -> this.revisionPersistence.updateCostByReference(revisionReference)
                .then());
    }
}
