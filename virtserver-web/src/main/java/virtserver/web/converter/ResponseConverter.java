package virtserver.web.converter;

import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ResponseConverter {

    private final Map<HttpStatus, String> mappedErrors;

    public ResponseConverter(final Map<HttpStatus, String> mappedErrors) {
        this.mappedErrors = mappedErrors;
    }

    public <X> ResponseEntity getResponse(final Either<HttpStatus, X> either, final HttpStatus httpStatus) {
        return either.fold(h -> new ResponseEntity<>(this.mappedErrors.get(h), h), x -> new ResponseEntity<>(x, httpStatus));
    }
}
