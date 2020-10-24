package virt.server.dto.error;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {

    private LocalDateTime currentTimestamp;
    private Type type;

    public ApiError(final Type type, final List<ErrorDetails> errors) {
        this.type = type;
        this.errors = errors;
        this.currentTimestamp = LocalDateTime.now();
    }

    private List<ErrorDetails> errors;
}
