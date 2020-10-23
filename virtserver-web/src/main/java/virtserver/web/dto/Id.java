package virtserver.web.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Id {
    
    private Integer id;

    @JsonCreator
    public Id(@JsonProperty("id") final Integer id) {
        this.id = id;
    }
}
