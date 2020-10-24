package virt.server.entities;


import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
public class AbstractEntity {

    @Column(name = "UPDATE_TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime updateTimestamp;
}
