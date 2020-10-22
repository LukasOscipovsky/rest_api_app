package virtserver.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@Data
public class Device extends MiniDevice {

    @ManyToOne
    @JoinColumn(name = "ZONE_ID", referencedColumnName = "ID")
    public Zone getZone() {
        return super.getZone();
    }
}
