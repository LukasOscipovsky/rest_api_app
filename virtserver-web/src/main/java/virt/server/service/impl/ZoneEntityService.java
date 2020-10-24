package virt.server.service.impl;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virt.server.aspect.Logging;
import virt.server.data.api.repository.Dao;
import virt.server.dto.Id;
import virt.server.dto.ZoneDTO;
import virt.server.entities.Zone;
import virt.server.service.EntityService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service("zoneEntityService")
@Slf4j
public class ZoneEntityService implements EntityService<ZoneDTO> {

    private final Dao<Zone> zoneRepository;
    private final ModelMapper modelMapper;

    public ZoneEntityService(final Dao<Zone> zoneRepository, final ModelMapper modelMapper) {
        this.zoneRepository = zoneRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Logging
    public Id create(final ZoneDTO zoneDTO) {
        final Zone zone = this.modelMapper.map(zoneDTO, Zone.class);

        this.zoneRepository.save(zone);

        log.debug("Zone with id [{}] has been stored successfully", zone.getId());

        return new Id(zone.getId());
    }

    @Override
    @Logging
    public Either<HttpStatus, Id> update(final ZoneDTO zoneDTO, final Long id) {
        final Zone zone = this.modelMapper.map(zoneDTO, Zone.class);

        zone.setId(id);

        return this.zoneRepository.update(zone).<Either<HttpStatus, Id>>map(z -> Either.right(new Id(z.getId())))
                .orElseGet(() -> Either.left(HttpStatus.NOT_FOUND));
    }

    @Override
    @Logging
    public Either<HttpStatus, ZoneDTO> get(final Long id) {
        final Optional<Zone> zone = this.zoneRepository.get(id);

        return zone.<Either<HttpStatus, ZoneDTO>>map(z -> Either.right(this.modelMapper.map(z, ZoneDTO.class)))
                .orElseGet(() -> Either.left(HttpStatus.NOT_FOUND));
    }

    @Override
    @Logging
    public List<ZoneDTO> getAll() {
        final List<Zone> zones = this.zoneRepository.getAll();
        return zones.stream().map(d -> this.modelMapper.map(d, ZoneDTO.class)).collect(Collectors.toList());
    }

    @Override
    @Logging
    public Either<HttpStatus, Id> delete(final Long id) {
        final boolean deleted = this.zoneRepository.delete(id);
        return deleted ? Either.right(new Id(id)) : Either.left(HttpStatus.NOT_FOUND);
    }
}
