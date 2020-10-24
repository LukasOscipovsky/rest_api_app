package virt.server.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import virt.server.data.api.config.DataApiConfig;
import virt.server.dto.error.ApiError;
import virt.server.dto.error.ErrorDetails;
import virt.server.dto.error.Type;
import virt.server.web.interceptor.LoggingInterceptor;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@EnableAspectJAutoProxy(proxyTargetClass = true)
@Import({SpringFoxConfig.class, DataApiConfig.class})
@Configuration
public class Config implements WebMvcConfigurer {

    private final LoggingInterceptor loggingInterceptor;

    public Config(final LoggingInterceptor loggingInterceptor) {
        this.loggingInterceptor = loggingInterceptor;
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry) {
        registry.addInterceptor(this.loggingInterceptor)
                .addPathPatterns("/**");
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Map<HttpStatus, ApiError> mappedErrors() {
        final Map<HttpStatus, ApiError> map = new EnumMap<>(HttpStatus.class);
        map.put(HttpStatus.NOT_FOUND, this.createErrorResponse());
        return map;
    }

    private ApiError createErrorResponse() {
        final ErrorDetails details = new ErrorDetails();
        details.setMessage("Entity not found");

        return new ApiError(Type.NOT_FOUND, Collections.singletonList(details));
    }
}
