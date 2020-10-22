package virtserver.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class HealthController {

    @GetMapping("/health/status")
    public HttpStatus health() {
        log.info("Health request received");
        return HttpStatus.OK;
    }
}
