package es.upm.miw.tfm.automundo.infrastructure.mongodb.persistence;

import es.upm.miw.tfm.automundo.domain.model.Technician;
import es.upm.miw.tfm.automundo.domain.persistence.TechnicianPersistence;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.daos.TechnicianReactive;
import es.upm.miw.tfm.automundo.infrastructure.mongodb.entities.TechnicianEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public class TechnicianPersistenceMongodb implements TechnicianPersistence {

    private TechnicianReactive technicianReactive;

    @Autowired
    public TechnicianPersistenceMongodb(TechnicianReactive technicianReactive) {
        this.technicianReactive = technicianReactive;
    }

    @Override
    public Flux<Technician> findByIdentificationIdAndNameAndSurNameAndActiveNullSafe(String identificationId, String name, String surName, Boolean active) {
        return this.technicianReactive.findByIdentificationIdAndNameAndSurNameAndActiveNullSafe(identificationId, name, surName, active)
                .map(TechnicianEntity::toTechnician);
    }
}