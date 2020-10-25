package virt.server.converter;

import io.vavr.control.Either;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import virt.server.dto.error.ApiError;
import virt.server.dto.error.ErrorDetails;
import virt.server.dto.error.Type;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResponseConverterTest {

    private static final String BODY = "body";
    private static final String FIELD_NAME = "fieldName";
    private static final String MESSAGE = "message";

    @Mock
    private Map<HttpStatus, ApiError> mappedErrors;

    @InjectMocks
    private ResponseConverter responseConverter;

    private Either<HttpStatus, String> either;
    private ApiError apiError;

    @BeforeEach
    void setUp() {
        final ErrorDetails errorDetails = new ErrorDetails(FIELD_NAME, MESSAGE);

        this.apiError = new ApiError(Type.NOT_FOUND, Collections.singletonList(errorDetails));

        this.either = Either.right(BODY);
    }

    @Test
    void getResponse() {
        final ResponseEntity response = this.responseConverter.getResponse(this.either, HttpStatus.OK);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Assertions.assertThat(response.getBody()).isEqualTo(BODY);
    }

    @Test
    void getResponse_left() {
        this.either = Either.left(HttpStatus.NOT_FOUND);

        when(this.mappedErrors.get(HttpStatus.NOT_FOUND)).thenReturn(this.apiError);

        final ResponseEntity response = this.responseConverter.getResponse(this.either, HttpStatus.OK);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}