package virt.server.data.api.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"virt.server.data.api.repository"})
@EntityScan("virt.server.entities")
public class DataApiConfig {

}
