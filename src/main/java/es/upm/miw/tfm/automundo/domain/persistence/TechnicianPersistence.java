package es.upm.miw.tfm.automundo.domain.persistence;

import es.upm.miw.tfm.automundo.domain.model.Technician;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface TechnicianPersistence {

    Flux<Technician> findByIdentificationIdAndNameAndSurNameAndActiveNullSafe(
            String identificationId, String name, String surName, Boolean active);
}
