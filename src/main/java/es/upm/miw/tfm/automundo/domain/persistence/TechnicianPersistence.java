package es.upm.miw.tfm.automundo.domain.persistence;

import es.upm.miw.tfm.automundo.domain.model.Technician;
import es.upm.miw.tfm.automundo.domain.model.TechnicianCreation;
import es.upm.miw.tfm.automundo.domain.model.TechnicianUpdate;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface TechnicianPersistence {

    Flux<Technician> findByIdentificationIdAndNameAndSurNameAndActiveNullSafe(
            String identificationId, String name, String surName, Boolean active);

    Mono<Technician> findByIdentificationId(String identification);

    Mono<Technician> create(TechnicianCreation technicianCreation);

    Mono<Technician> update(String identification, TechnicianUpdate technicianUpdate);
}
