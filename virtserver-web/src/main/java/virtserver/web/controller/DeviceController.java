package virtserver.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController("/devices")
public class DeviceController {

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Integer getDevice(@PathVariable Integer id) {
        return id;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer createDevice(@RequestBody String device) {
        return Integer.valueOf(device);
    }

    @PutMapping(value = "/id", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Integer updateDevice(@PathVariable Integer id, @RequestBody String zone) {
        return id;
    }

    @DeleteMapping(value = "/id", consumes = MediaType.APPLICATION_JSON_VALUE)
    public HttpStatus deleteDevice(@PathVariable Integer id) {
        return HttpStatus.OK;
    }
}
