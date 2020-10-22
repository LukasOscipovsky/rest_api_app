package virtserver.web.controller;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorResponse handleException(final MethodArgumentNotValidException ex) {

        final List<FieldError> errors = ex.getBindingResult().getFieldErrors();

        final List<ErrorResponse.ErrorDetails> errorDetails = new ArrayList<>();
        for (final FieldError fieldError : errors) {
            final ErrorResponse.ErrorDetails error = new ErrorResponse.ErrorDetails();
            error.setFieldName(fieldError.getField());
            error.setMessage(fieldError.getDefaultMessage());
            errorDetails.add(error);
        }

        final ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(errorDetails);

        return errorResponse;
    }
}

@Data
class ErrorResponse {

    private List<ErrorDetails> errors;

    @Data
    public static class ErrorDetails {
        private String fieldName;
        private String message;
    }
}
