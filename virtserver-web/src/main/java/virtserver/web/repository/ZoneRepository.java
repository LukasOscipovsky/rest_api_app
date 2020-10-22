package virtserver.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import virtserver.entities.Zone;

import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Integer> {

    Optional<Zone> findByName(String name);
}
