package virtserver.web.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;
import virtserver.web.deserializer.AddressDeserializer;


@Data
@NoArgsConstructor
public class MidDeviceDTO {

    @JsonDeserialize(using = AddressDeserializer.class)
    private String address;

    @Length(max = 64, message = "validation failed due to not having description in range length")
    private String description;

    @Range(min = 0, message = "validation failed due to not having positive zoneId")
    private int zoneId;
}
