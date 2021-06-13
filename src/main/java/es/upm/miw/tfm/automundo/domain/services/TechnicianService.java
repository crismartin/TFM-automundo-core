package es.upm.miw.tfm.automundo.domain.services;

import es.upm.miw.tfm.automundo.domain.model.Replacement;
import es.upm.miw.tfm.automundo.domain.model.Technician;
import es.upm.miw.tfm.automundo.domain.model.TechnicianCreation;
import es.upm.miw.tfm.automundo.domain.model.TechnicianUpdate;
import es.upm.miw.tfm.automundo.domain.persistence.TechnicianPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TechnicianService {

    private TechnicianPersistence technicianPersistence;

    @Autowired
    public TechnicianService(TechnicianPersistence technicianPersistence) {
        this.technicianPersistence = technicianPersistence;
    }

    public Flux<Technician> findByIdentificationIdAndNameAndSurNameAndActiveNullSafe(
            String identificationId, String name, String surName, Boolean active) {
        return this.technicianPersistence.findByIdentificationIdAndNameAndSurNameAndActiveNullSafe(
                identificationId, name, surName, active);
    }

    public Mono<Technician> read(String identification) {
        return this.technicianPersistence.findByIdentificationId(identification);
    }

    public Mono<Technician> create(TechnicianCreation technicianCreation) {
        return this.technicianPersistence.create(technicianCreation);
    }

    public Mono<Technician> update(String identification, TechnicianUpdate technicianUpdate) {
        return this.technicianPersistence.update(identification, technicianUpdate);
    }
}
