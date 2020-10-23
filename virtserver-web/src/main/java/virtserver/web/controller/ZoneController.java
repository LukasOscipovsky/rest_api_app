package virtserver.web.controller;

import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import virtserver.web.converter.ResponseConverter;
import virtserver.web.dto.Id;
import virtserver.web.dto.MiniZoneDTO;
import virtserver.web.dto.ZoneDTO;
import virtserver.web.service.EntityService;

import javax.validation.Valid;

// TODO solve issue with ambiguous
@RestController
public class ZoneController {

    private final EntityService<MiniZoneDTO, ZoneDTO> zoneService;
    private final ResponseConverter responseConverter;

    public ZoneController(@Qualifier("zoneEntityService") final EntityService<MiniZoneDTO, ZoneDTO> zoneService, final ResponseConverter responseConverter) {
        this.zoneService = zoneService;
        this.responseConverter = responseConverter;
    }

    @GetMapping(value = "/zones/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getZone(@PathVariable final Integer id) {
        final Either<HttpStatus, ZoneDTO> zone = this.zoneService.get(id);

        return this.responseConverter.getResponse(zone, HttpStatus.OK);
    }

    @PostMapping(value = "/zones", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createZone(@Valid @RequestBody final MiniZoneDTO zone) {
        final Either<HttpStatus, Id> id = this.zoneService.create(zone);

        return this.responseConverter.getResponse(id, HttpStatus.CREATED);
    }

    @PutMapping(value = "/zones/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateZone(@PathVariable final Integer id, @RequestBody @Valid final MiniZoneDTO zone) {
        final Either<HttpStatus, Id> either = this.zoneService.update(zone, id);

        return this.responseConverter.getResponse(either, HttpStatus.OK);
    }

    @DeleteMapping(value = "/zones/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteZone(@PathVariable final Integer id) {
        this.zoneService.delete(id);
        return HttpStatus.OK;
    }
}
