package virt.server.entities;


import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@MappedSuperclass
@Data
public class AbstractEntity {

    @Id
    @GeneratedValue
    @Column(name = "ID", nullable = false, updatable = false)
    private Long id;

    @Column(name = "UPDATE_TIMESTAMP")
    @UpdateTimestamp
    private LocalDateTime updateTimestamp;
}
