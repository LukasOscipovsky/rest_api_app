package virt.server.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;
import org.hibernate.validator.constraints.Range;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
@JsonIgnoreProperties(value = {"id", "identifier"}, allowGetters = true)
public class DeviceDTO extends MidDeviceDTO {

    @Delegate
    @JsonIgnore
    private final Identifier identifier = new Identifier();

    @Range(min = 0, message = "validation failed due to not having positive zoneId")
    private Long zoneId;
}
