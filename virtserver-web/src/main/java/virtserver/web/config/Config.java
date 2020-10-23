package virtserver.web.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import virtserver.data.api.config.DataApiConfig;
import virtserver.web.dto.error.ErrorResponse;

import java.util.HashMap;
import java.util.Map;

@Import(DataApiConfig.class)
@Configuration
@EntityScan("virtserver.entities")
public class Config {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Map<HttpStatus, ErrorResponse> mappedErrors() {
        final Map<HttpStatus, ErrorResponse> map = new HashMap<>();
        map.put(HttpStatus.NOT_FOUND, new ErrorResponse("Entity not found"));
        map.put(HttpStatus.CONFLICT, "Property already exists");
        return map;
    }

}
