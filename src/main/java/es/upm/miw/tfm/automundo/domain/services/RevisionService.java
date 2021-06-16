package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.domain.model.Revision;
import es.upm.miw.tfm.automundo.domain.persistence.RevisionPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RevisionService {

    private RevisionPersistence revisionPersistence;

    @Autowired
    public RevisionService(RevisionPersistence revisionPersistence){
        this.revisionPersistence = revisionPersistence;
    }

    public Flux<Revision> findAllByVehicleReference(String reference) {
        return revisionPersistence.findAllByVehicleReference(reference);
    }

    public Mono<Revision> create(Revision revision) {
        return revisionPersistence.create(revision);
    }
}
