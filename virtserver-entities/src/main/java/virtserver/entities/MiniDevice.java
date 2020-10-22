package virtserver.entities;

import javax.persistence.*;

@Entity
@Table(name = "DEVICE")
public class MiniDevice {

    @Embedded
    private MidDevice midDevice;

    private Zone zone;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "ZONE_ID", referencedColumnName = "ID", nullable = false)
    public Zone getZone() {
        return zone;
    }
}
