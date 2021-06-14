package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.exceptions.NotFoundException;
import es.upm.miw.tfm.automundo.domain.model.Revision;
import es.upm.miw.tfm.automundo.domain.persistence.RevisionPersistence;
import es.upm.miw.tfm.automundo.domain.persistence.VehiclePersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.RevisionReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.VehicleReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.RevisionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class RevisionPersistenceMongodb implements RevisionPersistence {

    private VehicleReactive vehicleReactive;
    private RevisionReactive revisionReactive;

    @Autowired
    public RevisionPersistenceMongodb(VehicleReactive vehicleReactive, RevisionReactive revisionReactive){
        this.vehicleReactive = vehicleReactive;
        this.revisionReactive = revisionReactive;
    }

    @Override
    public Flux<Revision> findAllByVehicleReference(String reference) {
        return vehicleReactive.findByReference(reference)
                .switchIfEmpty(Mono.error(new NotFoundException("Vehicle Reference: " + reference)))
                .flatMapMany(vehicleEntity -> this.revisionReactive.findAllByVehicleEntity(vehicleEntity)
                        .map(RevisionEntity::toRevision)
                );
    }
}
