package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.Technician;
import es.upm.miw.tfm.automundo.domain.services.TechnicianService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.TechnicianLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping(TechnicianResource.TECHNICIANS)
public class TechnicianResource {
    public static final String TECHNICIANS = "/technicians";
    public static final String SEARCH = "/search";
    public static final String IDENTIFICATION_ID = "/{identification}";

    private TechnicianService technicianService;
    @Autowired
    public TechnicianResource(TechnicianService technicianService) {
        this.technicianService = technicianService;
    }

    @GetMapping(SEARCH)
    public Flux<TechnicianLineDto> findByIdentificationIdAndNameAndSurNameAndActiveNullSafe(
            @RequestParam(required = false) String identificationId, @RequestParam(required = false) String name,
            @RequestParam(required = false) String surName, @RequestParam(required = false) Boolean active) {
        return this.technicianService.findByIdentificationIdAndNameAndSurNameAndActiveNullSafe(
                identificationId, name, surName, active)
                .map(TechnicianLineDto::new);
    }

    @GetMapping(IDENTIFICATION_ID)
    public Mono<Technician> read(@PathVariable String identification) {
        return this.technicianService.read(identification);
    }

}
