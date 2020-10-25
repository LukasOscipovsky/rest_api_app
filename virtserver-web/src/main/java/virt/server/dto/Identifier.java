package virt.server.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Identifier {

    private Long id;

    @JsonCreator
    public Identifier(@JsonProperty("id") final Long id) {
        this.id = id;
    }
}
