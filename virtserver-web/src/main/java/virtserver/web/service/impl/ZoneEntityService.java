package virtserver.web.service.impl;

import io.vavr.control.Either;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import virtserver.entities.Zone;
import virtserver.web.dto.MiniZone;
import virtserver.web.repository.ZoneRepository;
import virtserver.web.service.EntityService;

import java.util.Optional;

@Service("zoneEntityService")
public class ZoneEntityService implements EntityService<Zone, MiniZone> {

    private final ZoneRepository zoneRepository;
    private final ModelMapper modelMapper;

    public ZoneEntityService(final ZoneRepository zoneRepository, final ModelMapper modelMapper) {
        this.zoneRepository = zoneRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Either<HttpStatus, Integer> create(final MiniZone zone) {
        final Optional<Zone> zoneByName = this.zoneRepository.findByName(zone.getName());

        return zoneByName.<Either<HttpStatus, Integer>>map(z -> Either.left(HttpStatus.CONFLICT))
                .orElseGet(() -> this.saveAndReturnId(zone));
    }

    @Override
    public Either<HttpStatus, Integer> update(final MiniZone zone) {
        final Optional<Zone> zoneByName = this.zoneRepository.findByName(zone.getName());

        return zoneByName.<Either<HttpStatus, Integer>>map(z -> Either.left(HttpStatus.CONFLICT))
                .orElseGet(() -> this.saveAndReturnId(zone));
    }

    @Override
    public Either<HttpStatus, Zone> get(final Integer id) {
        final Optional<Zone> zone = this.zoneRepository.findById(id);

        return zone.<Either<HttpStatus, Zone>>map(Either::right)
                .orElseGet(() -> Either.left(HttpStatus.NOT_FOUND));
    }

    @Override
    public HttpStatus delete(final Integer id) {
        final Optional<Zone> deletedZone = this.zoneRepository.findById(id);

        return deletedZone.map(z -> HttpStatus.OK).orElse(HttpStatus.NOT_FOUND);
    }

    private Either<HttpStatus, Integer> saveAndReturnId(final MiniZone miniZone) {
        final Zone zone = this.modelMapper.map(miniZone, Zone.class);

        final Integer id = this.zoneRepository.save(zone).getId();

        return Either.right(id);
    }
}
