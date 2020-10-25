package virt.server.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virt.server.data.api.repository.Dao;
import virt.server.dto.DeviceDTO;
import virt.server.entities.Device;
import virt.server.service.EntityService;

@Transactional
@Service("deviceEntityService")
@Slf4j
public class DeviceEntityService extends AbstractEntityService<DeviceDTO, Device> implements EntityService<DeviceDTO> {

    private final Dao<Device> deviceRepository;

    public DeviceEntityService(final Dao<Device> deviceRepository, final ModelMapper modelMapper) {
        super(modelMapper);
        this.deviceRepository = deviceRepository;
    }

    @Override
    protected Dao<Device> getRepository() {
        return this.deviceRepository;
    }
}
