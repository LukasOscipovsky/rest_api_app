package virt.server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@NoArgsConstructor
public class MiniZoneDTO {

    @Length(min = 2, max = 32, message = "validation failed due to not having name in range length")
    private String name;

    @Length(max = 64, message = "validation failed due to not having description in range length")
    private String description;
}
