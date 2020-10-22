package virtserver.entities;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "ZONE")
@Data
public class Zone extends MiniZone {

    @Id
    @GeneratedValue
    private Integer id;

    @Embedded
    private MiniZone miniZone;

    @OneToMany(mappedBy = "device")
    private List<Device> devices;
}
