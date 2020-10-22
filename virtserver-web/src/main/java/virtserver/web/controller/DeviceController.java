package virtserver.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class DeviceController {

    @GetMapping(value = "/devices/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer getDevice(@PathVariable final Integer id) {
        return id;
    }

    @PostMapping(value = "/devices", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer createDevice(@RequestBody final String device) {
        return Integer.valueOf(device);
    }

    @PutMapping(value = "/devices/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer updateDevice(@PathVariable final Integer id, @RequestBody final String zone) {
        return id;
    }

    @DeleteMapping(value = "/devices/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteDevice(@PathVariable final Integer id) {
        return HttpStatus.OK;
    }
}
