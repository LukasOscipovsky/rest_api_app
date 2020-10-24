package virt.server.web.controller;

import org.springframework.core.NestedRuntimeException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import springfox.documentation.annotations.ApiIgnore;
import virt.server.dto.error.ApiError;
import virt.server.dto.error.ErrorDetails;
import virt.server.dto.error.Type;

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@ApiIgnore
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleMANVException(final MethodArgumentNotValidException ex) {

        final List<ErrorDetails> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> new ErrorDetails(e.getField(), e.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ApiError(Type.VALIDATION_ERROR, errors);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiError handleHMNRException(final HttpMessageNotReadableException ex) {
        return this.getErrorResponse(ex.getRootCause(), ex.getMessage(), Type.VALIDATION_ERROR);
    }

    @ExceptionHandler(value = {EmptyResultDataAccessException.class, EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ApiError handleERDAException(final NestedRuntimeException ex) {
        return this.getErrorResponse(ex.getRootCause(), ex.getMessage(), Type.NOT_FOUND);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    @ResponseBody
    public ApiError handleConstraintException(final DataIntegrityViolationException ex) {
        return this.getErrorResponse(null, ex.getMostSpecificCause().getMessage(), Type.INTEGRITY_CONSTRAINT);
    }

    private ApiError getErrorResponse(final Throwable rootCause, final String message2, final Type type) {
        final String message = rootCause != null ? rootCause.getMessage() : message2;

        final ErrorDetails details = new ErrorDetails();
        details.setMessage(message);

        return new ApiError(type, Collections.singletonList(details));
    }
}
