package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.model.Replacement;
import es.upm.miw.tfm.automundo.domain.services.ReplacementService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.ReplacementLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(ReplacementResource.REPLACEMENTS)
public class ReplacementResource {
    public static final String REPLACEMENTS = "/replacements";
    public static final String SEARCH = "/search";

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
}
