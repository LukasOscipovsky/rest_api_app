package virtserver.web.controller;

import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import virtserver.web.dto.error.ErrorResponse;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleMANVException(final MethodArgumentNotValidException ex) {
        
        final List<ErrorResponse.ErrorDetails> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> new ErrorResponse.ErrorDetails(e.getField(), e.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ErrorResponse(errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleHMNRException(final HttpMessageNotReadableException ex) {
        return this.getErrorResponse(ex.getRootCause(), ex.getMessage());
    }

    @ExceptionHandler(value = {EmptyResultDataAccessException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorResponse handleERDAException(final NestedRuntimeException ex) {
        return this.getErrorResponse(ex.getRootCause(), ex.getMessage());
    }

    private ErrorResponse getErrorResponse(final Throwable rootCause, final String message2) {
        final String message = rootCause != null ? rootCause.getMessage() : message2;

        final ErrorResponse.ErrorDetails details = new ErrorResponse.ErrorDetails(message);

        return new ErrorResponse(Collections.singletonList(details));
    }
}
