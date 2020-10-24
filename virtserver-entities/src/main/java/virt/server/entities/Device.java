package virt.server.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "DEVICE")
@Data
@EqualsAndHashCode(callSuper = true)
public class Device extends AbstractEntity {

    @Column(name = "ADDRESS", nullable = false, unique = true)
    private String address;

    @Column(name = "DESCRIPTION")
    private String description;

    @ManyToOne
    @JoinColumn(name = "ZONE_ID", referencedColumnName = "ID")
    private Zone zone;
}
