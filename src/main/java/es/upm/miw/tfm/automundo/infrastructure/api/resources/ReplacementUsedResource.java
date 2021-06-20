package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.ReplacementUsed;
import es.upm.miw.tfm.automundo.domain.services.ReplacementUsedService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.ReplacementUsedDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

@RestController
@RequestMapping(ReplacementUsedResource.REPLACEMENTS_USED)
public class ReplacementUsedResource {

    public static final String REPLACEMENTS_USED = "/replacements-used";
    public static final String REVISION_REFERENCE= "/revision";
    public static final String REFERENCE = "/{reference}";

    private ReplacementUsedService replacementUsedService;

    @Autowired
    public ReplacementUsedResource(ReplacementUsedService replacementUsedService){
        this.replacementUsedService = replacementUsedService;
    }

    @PutMapping
    public Mono<ReplacementUsed> update(@Valid @RequestBody ReplacementUsedDto replacementUsedUpdated){
        return replacementUsedService.update(new ReplacementUsed(replacementUsedUpdated));
    }

    @PostMapping(produces = {"application/json"})
    public Mono<ReplacementUsed> create(@Valid @RequestBody ReplacementUsedDto replacementUsedUpdated){
        return replacementUsedService.create(new ReplacementUsed(replacementUsedUpdated));
    }

    @GetMapping(REVISION_REFERENCE)
    public Flux<ReplacementUsed> findAllByRevisionReference(@RequestParam String reference) {
        return replacementUsedService.findAllByRevisionReference(reference);
    }

    @DeleteMapping(REFERENCE)
    public Mono<Void> delete(@PathVariable String reference) {
        return replacementUsedService.delete(reference);
    }
}
