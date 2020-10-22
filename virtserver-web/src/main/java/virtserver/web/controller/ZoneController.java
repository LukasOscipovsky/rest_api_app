package virtserver.web.controller;

import io.vavr.control.Either;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import virtserver.entities.Zone;
import virtserver.web.service.EntityService;

@RestController("/zones")
public class ZoneController {

    private final EntityService<Zone> zoneService;

    public ZoneController(@Qualifier("zoneEntityService") EntityService<Zone> zoneService) {
        this.zoneService = zoneService;
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getZone(@PathVariable Integer id) {
        Either<HttpStatus, Zone> zone = zoneService.get(id);

        return zone.isRight() ? new ResponseEntity(zone.get(), HttpStatus.CREATED) : new ResponseEntity(zone.getLeft());
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer createZone(@RequestBody Zone zone) {
        return 1;
    }

    @PutMapping(value = "/id", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer updateZone(@PathVariable Integer id, @RequestBody String zone) {
        return id;
    }

    @DeleteMapping(value = "/id", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteZone(@PathVariable Integer id) {
        return HttpStatus.OK;
    }
}
