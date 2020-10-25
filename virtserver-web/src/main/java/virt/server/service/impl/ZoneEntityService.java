package virt.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virt.server.data.api.repository.Dao;
import virt.server.dto.ZoneDTO;
import virt.server.entities.Zone;
import virt.server.service.EntityService;

@Transactional
@Service("zoneEntityService")
@Slf4j
public class ZoneEntityService extends AbstractEntityService<ZoneDTO, Zone> implements EntityService<ZoneDTO> {

    private final Dao<Zone> zoneRepository;

    public ZoneEntityService(final Dao<Zone> zoneRepository, final ModelMapper modelMapper) {
        super(modelMapper);
        this.zoneRepository = zoneRepository;
    }

    @Override
    protected Dao<Zone> getRepository() {
        return this.zoneRepository;
    }
}
