package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.Replacement;
import es.upm.miw.tfm.automundo.domain.model.ReplacementCreation;
import es.upm.miw.tfm.automundo.domain.model.ReplacementUpdate;
import es.upm.miw.tfm.automundo.domain.services.ReplacementService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.ReplacementLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(ReplacementResource.REPLACEMENTS)
public class ReplacementResource {
    public static final String REPLACEMENTS = "/replacements";
    public static final String SEARCH = "/search";
    public static final String REFERENCE = "/{reference}";

    private ReplacementService replacementService;

    @Autowired
    public ReplacementResource(ReplacementService replacementService) {
        this.replacementService = replacementService;
    }

    @GetMapping(SEARCH)
    public Flux<ReplacementLineDto> findByReferenceAndNameAndDescriptionNullSafe(
            @RequestParam(required = false) String reference, @RequestParam(required = false) String name, @RequestParam(required = false) String description) {
        return this.replacementService.findByReferenceAndNameAndDescriptionNullSafe(reference, name, description)
                .map(ReplacementLineDto::new);
    }

    @GetMapping(REFERENCE)
    public Mono<Replacement> read(@PathVariable String reference) {
        return this.replacementService.read(reference);
    }

    @PostMapping(produces = {"application/json"})
    public Mono<Replacement> create(@Valid @RequestBody ReplacementCreation replacementCreation) {
        return this.replacementService.create(replacementCreation);
    }

    @PutMapping(REFERENCE)
    public Mono<Replacement> update(@PathVariable String reference, @Valid @RequestBody ReplacementUpdate replacementUpdate) {
        return this.replacementService.update(reference, replacementUpdate);
    }
}
