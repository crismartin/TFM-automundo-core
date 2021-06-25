package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.domain.model.Revision;
import es.upm.miw.tfm.automundo.domain.persistence.RevisionPersistence;
import es.upm.miw.tfm.automundo.domain.services.utils.PdfInvoiceBuilder;
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

    public Mono<Revision> createReplacementsUsed(Revision revision) {
        return revisionPersistence.createReplacementsUsed(revision);
    }

    public Mono<Revision> findByReference(String reference) {
        return revisionPersistence.findByReference(reference);
    }

    public Mono<Revision> update(Revision revision) {
        return revisionPersistence.update(revision);
    }

    public Mono<byte[]> printByReference(String reference) {
        return this.revisionPersistence.findByReference(reference)
                .map(new PdfInvoiceBuilder()::generateInvoice);
    }

    public Mono<Void> deleteLogic(String reference) {
        return this.revisionPersistence.deleteLogic(reference);
    }
}
