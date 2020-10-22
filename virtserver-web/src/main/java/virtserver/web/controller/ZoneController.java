package virtserver.web.controller;

import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import virtserver.entities.Zone;
import virtserver.web.dto.MiniZone;
import virtserver.web.service.EntityService;

import javax.validation.Valid;

// TODO solve issue with ambiguous
@RestController
public class ZoneController {

    private final EntityService<Zone, MiniZone> zoneService;

    public ZoneController(@Qualifier("zoneEntityService") final EntityService<Zone, MiniZone> zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping(value = "/zones/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getZone(@PathVariable final Integer id) {
        final Either<HttpStatus, Zone> zone = this.zoneService.get(id);

        return this.getResponse(zone, HttpStatus.CREATED);
    }

    @PostMapping(value = "/zones", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createZone(@RequestBody @Valid final MiniZone zone) {
        final Either<HttpStatus, Integer> id = this.zoneService.create(zone);

        return this.getResponse(id, HttpStatus.OK);
    }

    @PutMapping(value = "/zones/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer updateZone(@PathVariable final Integer id, @RequestBody @Valid final MiniZone zone) {
        return id;
    }

    @DeleteMapping(value = "/zones/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteZone(@PathVariable final Integer id) {
        return HttpStatus.OK;
    }

    private <X> ResponseEntity getResponse(final Either<HttpStatus, X> either, final HttpStatus httpStatus) {
        return either.isRight() ? new ResponseEntity<>(either.get(), httpStatus) : new ResponseEntity<HttpStatus>(either.getLeft());
    }
}
