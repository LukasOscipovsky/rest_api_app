package virt.server.service.impl;

import io.vavr.control.Either;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import virt.server.aspect.Logging;
import virt.server.data.api.repository.Dao;
import virt.server.dto.DeviceDTO;
import virt.server.dto.Id;
import virt.server.entities.Device;
import virt.server.service.EntityService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service("deviceEntityService")
@Slf4j
public class DeviceEntityService implements EntityService<DeviceDTO> {

    private final Dao<Device> deviceRepository;
    private final ModelMapper modelMapper;

    public DeviceEntityService(final Dao<Device> deviceRepository, final ModelMapper modelMapper) {
        this.deviceRepository = deviceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Logging
    public Id create(final DeviceDTO deviceDTO) {
        final Device device = this.modelMapper.map(deviceDTO, Device.class);

        this.deviceRepository.save(device);

        return new Id(device.getId());
    }

    @Override
    @Logging
    public Either<HttpStatus, Id> update(final DeviceDTO deviceDTO, final Long id) {
        final Device device = this.modelMapper.map(deviceDTO, Device.class);

        device.setId(id);

        return this.deviceRepository.update(device).<Either<HttpStatus, Id>>map(d -> Either.right(new Id(d.getId())))
                .orElseGet(() -> Either.left(HttpStatus.NOT_FOUND));
    }

    @Override
    @Logging
    public Either<HttpStatus, DeviceDTO> get(final Long id) {
        final Optional<Device> device = this.deviceRepository.get(id);

        return device.<Either<HttpStatus, DeviceDTO>>map(d -> Either.right(this.modelMapper.map(d, DeviceDTO.class)))
                .orElseGet(() -> Either.left(HttpStatus.NOT_FOUND));
    }

    @Override
    @Logging
    public List<DeviceDTO> getAll() {
        final List<Device> devices = this.deviceRepository.getAll();
        return devices.stream().map(d -> this.modelMapper.map(d, DeviceDTO.class)).collect(Collectors.toList());
    }

    @Override
    @Logging
    public Either<HttpStatus, Id> delete(final Long id) {
        final boolean deleted = this.deviceRepository.delete(id);

        return deleted ? Either.right(new Id(id)) : Either.left(HttpStatus.NOT_FOUND);
    }
}
