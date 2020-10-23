package virtserver.web.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
public class ErrorResponse {

    private List<ErrorDetails> errors;

    @Data
    @RequiredArgsConstructor
    @AllArgsConstructor
    public static class ErrorDetails {
        private String fieldName;
        private final String message;
    }
}
