package virt.server.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Id {

    private Long id;

    @JsonCreator
    public Id(@JsonProperty("id") final Long id) {
        this.id = id;
    }
}
