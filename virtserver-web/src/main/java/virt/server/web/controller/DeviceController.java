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
import virt.server.dto.DeviceDTO;
import virt.server.dto.Id;
import virt.server.service.EntityService;

import javax.validation.Valid;
import java.util.List;

@RestController
public class DeviceController {

    private final EntityService<DeviceDTO> deviceService;
    private final ResponseConverter responseConverter;

    public DeviceController(@Qualifier("deviceEntityService") final EntityService<DeviceDTO> deviceService, final ResponseConverter responseConverter) {
        this.deviceService = deviceService;
        this.responseConverter = responseConverter;
    }

    @GetMapping(value = "/devices/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getDevice(@PathVariable final Long id) {
        final Either<HttpStatus, DeviceDTO> zone = this.deviceService.get(id);

        return this.responseConverter.getResponse(zone, HttpStatus.OK);
    }

    @GetMapping(value = "/devices", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        return new ResponseEntity<>(this.deviceService.getAll(), HttpStatus.OK);
    }

    @PostMapping(value = "/devices", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Id> createDevice(@Valid @RequestBody final DeviceDTO device) {
        return new ResponseEntity<>(this.deviceService.create(device), HttpStatus.CREATED);
    }

    @PutMapping(value = "/devices/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateDevice(@PathVariable final Long id, @Valid @RequestBody final DeviceDTO device) {
        final Either<HttpStatus, Id> either = this.deviceService.update(device, id);

        return this.responseConverter.getResponse(either, HttpStatus.OK);
    }

    @DeleteMapping(value = "/devices/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Id> deleteDevice(@PathVariable final Long id) {
        final Either<HttpStatus, Id> either = this.deviceService.delete(id);
        return this.responseConverter.getResponse(either, HttpStatus.OK);
    }
}
