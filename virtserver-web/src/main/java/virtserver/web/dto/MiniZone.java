package virtserver.web.dto;

import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class MiniZone {
    @Length(min = 2, max = 32, message = "validation failed due to not having name in range length")
    private String name;

    @Length(max = 64)
    private String description;
}
