package virtserver.entities;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DEVICE")
@Data
public class Device {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, updatable = false)
    private Integer id;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "ZONE_ID", referencedColumnName = "ID")
    private Zone zone;
}
