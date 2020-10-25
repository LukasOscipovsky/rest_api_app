package virt.server.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.Delegate;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@JsonIgnoreProperties(value = {"id", "identifier", "devices"}, allowGetters = true)
public class ZoneDTO extends MiniZoneDTO {

    @Delegate
    @JsonIgnore
    private Identifier identifier = new Identifier();

    private List<DeviceDTO> devices;
}
