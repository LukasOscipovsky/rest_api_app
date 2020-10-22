package virtserver.data.api.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"virtserver.data.api.repository"})
public class DataApiConfig {
}
