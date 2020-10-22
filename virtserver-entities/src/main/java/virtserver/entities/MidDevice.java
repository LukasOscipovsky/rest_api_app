package virtserver.entities;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Embeddable
public class MidDevice {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(name = "ADDRESS")
    private String address;
}
