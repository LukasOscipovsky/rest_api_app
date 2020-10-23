package virtserver.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class ZoneDTO extends MiniZoneDTO {

    @Delegate
    @JsonIgnore
    private Id identifier = new Id();

    private List<DeviceDTO> devices;
}
