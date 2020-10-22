package virtserver.web.service.impl;

import io.vavr.control.Either;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import virtserver.data.api.repository.ZoneRepository;
import virtserver.entities.Zone;
import virtserver.web.service.EntityService;

import java.util.Optional;

@Service("zoneEntityService")
public class ZoneEntityService implements EntityService<Zone> {

    private final ZoneRepository zoneRepository;

    public ZoneEntityService(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @Override
    public Either<HttpStatus, Integer> create(Zone zone) {
        Optional<Zone> zoneByName = zoneRepository.findByName(zone.getName());

        return zoneByName.<Either<HttpStatus, Integer>>map(z -> Either.left(HttpStatus.CONFLICT))
                .orElseGet(() -> this.saveAndReturnId(zone));
    }

    @Override
    public Either<HttpStatus, Integer> update(Zone zone) {
        Optional<Zone> zoneByName = zoneRepository.findByName(zone.getName());

        return zoneByName.<Either<HttpStatus, Integer>>map(z -> Either.left(HttpStatus.CONFLICT))
                .orElseGet(() -> this.saveAndReturnId(zone));
    }

    @Override
    public Either<HttpStatus, Zone> get(Integer id) {
        Optional<Zone> zone = zoneRepository.findById(id);

        return zone.<Either<HttpStatus, Zone>>map(Either::right)
                .orElseGet(() -> Either.left(HttpStatus.NOT_FOUND));
    }

    @Override
    public HttpStatus delete(Integer id) {
        Optional<Zone> deletedZone = zoneRepository.findById(id);

        return deletedZone.map(z -> HttpStatus.OK).orElse(HttpStatus.NOT_FOUND);
    }

    private Either<HttpStatus, Integer> saveAndReturnId(Zone zone) {
        Integer id = zoneRepository.save(zone).getId();

        return Either.right(id);
    }
}
