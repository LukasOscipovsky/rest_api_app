package virtserver.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
public class DeviceDTO extends MidDeviceDTO {

    @Delegate
    @JsonIgnore
    private final Id identifier = new Id();
}
