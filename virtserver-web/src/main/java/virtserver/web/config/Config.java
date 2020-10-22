package virtserver.web.config;

import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import virtserver.data.api.config.DataApiConfig;

@Import(DataApiConfig.class)
@Configuration
@EntityScan("virtserver.entities")
public class Config {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
