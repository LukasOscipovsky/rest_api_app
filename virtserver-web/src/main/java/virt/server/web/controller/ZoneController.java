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
import virt.server.dto.Identifier;
import virt.server.dto.ZoneDTO;
import virt.server.dto.error.ApiError;
import virt.server.service.EntityService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/zones")
@Api(tags = "zones")
@ApiResponses(value = {
        @ApiResponse(code = 400, message = "Request is invalid or validation on field properties failed", response = ApiError.class),
        @ApiResponse(code = 404, message = "Entity not found", response = ApiError.class),
        @ApiResponse(code = 409, message = "Integrity Violation for entity", response = ApiError.class),
        @ApiResponse(code = 500, message = "Internal Server error", response = ApiError.class)
})
public class ZoneController {

    private final EntityService<ZoneDTO> zoneService;
    private final ResponseConverter responseConverter;

    public ZoneController(@Qualifier("zoneEntityService") final EntityService<ZoneDTO> zoneService, final ResponseConverter responseConverter) {
        this.zoneService = zoneService;
        this.responseConverter = responseConverter;
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Zone successfully retrieved", response = ZoneDTO.class),
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getZone(@PathVariable final Long id) {
        final Either<HttpStatus, ZoneDTO> zone = this.zoneService.get(id);

        return this.responseConverter.getResponse(zone, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Zones successfully retrieved", response = ZoneDTO[].class),
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ZoneDTO>> getAllZones() {
        return new ResponseEntity<>(this.zoneService.getAll(), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Zone successfully created", response = Identifier.class),
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Identifier> createZone(@Valid @RequestBody final ZoneDTO zone) {
        return new ResponseEntity<>(this.zoneService.create(zone), HttpStatus.CREATED);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Zone successfully updated", response = Identifier.class),
    })
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity updateZone(@PathVariable final Long id, @RequestBody @Valid final ZoneDTO zone) {
        final Either<HttpStatus, Identifier> either = this.zoneService.update(zone, id);

        return this.responseConverter.getResponse(either, HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Zone successfully deleted", response = Identifier.class),
    })
    @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity deleteZone(@PathVariable final Long id) {
        final Either<HttpStatus, Identifier> either = this.zoneService.delete(id);
        return this.responseConverter.getResponse(either, HttpStatus.OK);
    }
}
