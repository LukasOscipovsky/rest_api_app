package virtserver.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import virtserver.entities.Device;

import java.util.Optional;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Integer> {

    Optional<Device> findByAddress(String address);
}
