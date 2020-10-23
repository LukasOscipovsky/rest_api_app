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
import virtserver.web.dto.DeviceDTO;
import virtserver.web.dto.Id;
import virtserver.web.dto.MidDeviceDTO;
import virtserver.web.service.EntityService;

import javax.validation.Valid;

@RestController
public class DeviceController {

    private final EntityService<MidDeviceDTO, DeviceDTO> deviceService;
    private final ResponseConverter responseConverter;

    public DeviceController(@Qualifier("deviceEntityService") final EntityService<MidDeviceDTO, DeviceDTO> deviceService, final ResponseConverter responseConverter) {
        this.deviceService = deviceService;
        this.responseConverter = responseConverter;
    }

    @GetMapping(value = "/devices/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getDevice(@PathVariable final Integer id) {
        final Either<HttpStatus, DeviceDTO> zone = this.deviceService.get(id);

        return this.responseConverter.getResponse(zone, HttpStatus.CREATED);
    }

    @PostMapping(value = "/devices", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity createDevice(@Valid @RequestBody final MidDeviceDTO device) {
        final Either<HttpStatus, Id> id = this.deviceService.create(device);

        return this.responseConverter.getResponse(id, HttpStatus.CREATED);
    }

    @PutMapping(value = "/devices/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateDevice(@PathVariable final Integer id, @Valid @RequestBody final MidDeviceDTO midDeviceDTO) {
        final Either<HttpStatus, Id> either = this.deviceService.update(midDeviceDTO, id);

        return this.responseConverter.getResponse(either, HttpStatus.CREATED);
    }

    @DeleteMapping(value = "/devices/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteDevice(@PathVariable final Integer id) {
        this.deviceService.delete(id);
        return HttpStatus.OK;
    }
}
