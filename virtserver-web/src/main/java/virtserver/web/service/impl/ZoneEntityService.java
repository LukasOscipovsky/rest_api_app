package virtserver.web.service.impl;

import io.vavr.control.Either;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import virtserver.entities.Zone;
import virtserver.web.dto.Id;
import virtserver.web.dto.MiniZoneDTO;
import virtserver.web.dto.ZoneDTO;
import virtserver.web.repository.ZoneRepository;
import virtserver.web.service.EntityService;

import java.util.Optional;

@Service("zoneEntityService")
public class ZoneEntityService implements EntityService<MiniZoneDTO, ZoneDTO> {

    private final ZoneRepository zoneRepository;
    private final ModelMapper modelMapper;

    public ZoneEntityService(final ZoneRepository zoneRepository, final ModelMapper modelMapper) {
        this.zoneRepository = zoneRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Either<HttpStatus, Id> create(final MiniZoneDTO zone) {
        final Optional<Zone> zoneByName = this.zoneRepository.findByName(zone.getName());

        return zoneByName.<Either<HttpStatus, Id>>map(z -> Either.left(HttpStatus.CONFLICT))
                .orElseGet(() -> this.saveAndReturnId(zone));
    }

    @Override
    public Either<HttpStatus, Id> update(final MiniZoneDTO zone, final Integer id) {
        final Optional<Zone> zoneByName = this.zoneRepository.findByName(zone.getName());

        return zoneByName.<Either<HttpStatus, Id>>map(z -> Either.left(HttpStatus.CONFLICT))
                .orElseGet(() -> this.saveAndReturnId(zone, id));
    }

    @Override
    public Either<HttpStatus, ZoneDTO> get(final Integer id) {
        final Optional<Zone> zone = this.zoneRepository.findById(id);

        return zone.<Either<HttpStatus, ZoneDTO>>map(z -> Either.right(this.modelMapper.map(z, ZoneDTO.class)))
                .orElseGet(() -> Either.left(HttpStatus.NOT_FOUND));
    }

    @Override
    public void delete(final Integer id) {
        this.zoneRepository.deleteById(id);
    }

    private Either<HttpStatus, Id> saveAndReturnId(final MiniZoneDTO miniZone) {
        final Zone zone = this.modelMapper.map(miniZone, Zone.class);

        final Integer id = this.zoneRepository.saveAndFlush(zone).getId();

        return Either.right(new Id(id));
    }

    private Either<HttpStatus, Id> saveAndReturnId(final MiniZoneDTO miniZone, final Integer id) {
        final Zone zone = this.modelMapper.map(miniZone, Zone.class);

        zone.setId(id);

        this.zoneRepository.saveAndFlush(zone);

        return Either.right(new Id(id));
    }
}
