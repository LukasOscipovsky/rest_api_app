package virt.server.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import virt.server.converter.ResponseConverter;
import virt.server.dto.DeviceDTO;
import virt.server.dto.Identifier;
import virt.server.dto.error.ApiError;
import virt.server.service.EntityService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/devices")
@Api(tags = "devices")
@ApiResponses(value = {
        @ApiResponse(code = 400, message = "Request is invalid or validation on field properties failed", response = ApiError.class),
        @ApiResponse(code = 404, message = "Entity not found", response = ApiError.class),
        @ApiResponse(code = 409, message = "Integrity Violation for entity", response = ApiError.class),
        @ApiResponse(code = 500, message = "Internal Server error", response = ApiError.class)
})
public class DeviceController {

    private final EntityService<DeviceDTO> deviceService;
    private final ResponseConverter responseConverter;

    public DeviceController(@Qualifier("deviceEntityService") final EntityService<DeviceDTO> deviceService, final ResponseConverter responseConverter) {
        this.deviceService = deviceService;
        this.responseConverter = responseConverter;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Device successfully retrieved", response = DeviceDTO.class),
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getDevice(@PathVariable final Long id) {
        final Either<HttpStatus, DeviceDTO> zone = this.deviceService.get(id);

        return this.responseConverter.getResponse(zone, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Devices successfully retrieved", response = DeviceDTO[].class),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DeviceDTO>> getAllDevices() {
        return new ResponseEntity<>(this.deviceService.getAll(), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Device successfully created", response = Identifier.class),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Identifier> createDevice(@Valid @RequestBody final DeviceDTO device) {
        return new ResponseEntity<>(this.deviceService.create(device), HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Device successfully updated", response = Identifier.class),
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateDevice(@PathVariable final Long id, @Valid @RequestBody final DeviceDTO device) {
        final Either<HttpStatus, Identifier> either = this.deviceService.update(device, id);

        return this.responseConverter.getResponse(either, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Device successfully deleted", response = Identifier.class),
    })
    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteDevice(@PathVariable final Long id) {
        final Either<HttpStatus, Identifier> either = this.deviceService.delete(id);
        return this.responseConverter.getResponse(either, HttpStatus.OK);
    }
}
