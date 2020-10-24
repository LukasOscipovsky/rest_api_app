package virt.server.web.controller;

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
import virt.server.converter.ResponseConverter;
import virt.server.dto.Id;
import virt.server.dto.ZoneDTO;
import virt.server.service.EntityService;

import javax.validation.Valid;
import java.util.List;

// TODO solve issue with ambiguous
@RestController
public class ZoneController {

    private final EntityService<ZoneDTO> zoneService;
    private final ResponseConverter responseConverter;

    public ZoneController(@Qualifier("zoneEntityService") final EntityService<ZoneDTO> zoneService, final ResponseConverter responseConverter) {
        this.zoneService = zoneService;
        this.responseConverter = responseConverter;
    }

    @GetMapping(value = "/zones/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getZone(@PathVariable final Long id) {
        final Either<HttpStatus, ZoneDTO> zone = this.zoneService.get(id);

        return this.responseConverter.getResponse(zone, HttpStatus.OK);
    }

    @GetMapping(value = "/zones", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ZoneDTO>> getAllZones() {
        return new ResponseEntity<>(this.zoneService.getAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/zones", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Id> createZone(@Valid @RequestBody final ZoneDTO zone) {
        return new ResponseEntity<>(this.zoneService.create(zone), HttpStatus.OK);
    }

    @PutMapping(value = "/zones/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateZone(@PathVariable final Long id, @RequestBody @Valid final ZoneDTO zone) {
        final Either<HttpStatus, Id> either = this.zoneService.update(zone, id);

        return this.responseConverter.getResponse(either, HttpStatus.OK);
    }

    @DeleteMapping(value = "/zones/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteZone(@PathVariable final Long id) {
        final Either<HttpStatus, Id> either = this.zoneService.delete(id);
        return this.responseConverter.getResponse(either, HttpStatus.OK);
    }
}
