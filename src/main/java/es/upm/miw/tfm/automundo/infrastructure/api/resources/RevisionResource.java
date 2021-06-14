package es.upm.miw.tfm.automundo.infrastructure.api.resources;

import es.upm.miw.tfm.automundo.domain.services.RevisionService;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.RevisionLineDto;
import es.upm.miw.tfm.automundo.infrastructure.api.dtos.VehicleLineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(RevisionResource.REVISIONS)
public class RevisionResource {
    public static final String REVISIONS = "/revisions";
    public static final String VEHICLE_REFERENCE = "/vehicle/{reference}";
    public static final String REFERENCE = "/{reference}";

    private RevisionService revisionService;

    @Autowired
    public RevisionResource(RevisionService revisionService){
        this.revisionService = revisionService;
    }

    @GetMapping(VEHICLE_REFERENCE)
    public Flux<RevisionLineDto> findAllByVehicleReference(@PathVariable String reference) {
        return this.revisionService.findAllByVehicleReference(reference)
                .map(RevisionLineDto::new);
    }

}
