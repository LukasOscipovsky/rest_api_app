package virtserver.web.service.impl;

import io.vavr.control.Either;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import virtserver.entities.Device;
import virtserver.web.dto.DeviceDTO;
import virtserver.web.dto.Id;
import virtserver.web.dto.MidDeviceDTO;
import virtserver.web.repository.DeviceRepository;
import virtserver.web.service.EntityService;

import java.util.Optional;

@Service("deviceEntityService")
public class DeviceEntityService implements EntityService<MidDeviceDTO, DeviceDTO> {

    private final DeviceRepository deviceRepository;
    private final ModelMapper modelMapper;

    public DeviceEntityService(final DeviceRepository deviceRepository, final ModelMapper modelMapper) {
        this.deviceRepository = deviceRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Either<HttpStatus, Id> create(final MidDeviceDTO deviceDTO) {
        final Optional<Device> deviceByAddress = this.deviceRepository.findByAddress(deviceDTO.getAddress());

        return deviceByAddress.<Either<HttpStatus, Id>>map(z -> Either.left(HttpStatus.CONFLICT))
                .orElseGet(() -> this.saveAndReturnId(deviceDTO));
    }

    @Override
    public Either<HttpStatus, Id> update(final MidDeviceDTO deviceDTO, final Integer id) {
        final Optional<Device> deviceByAddress = this.deviceRepository.findByAddress(deviceDTO.getAddress());

        return deviceByAddress.<Either<HttpStatus, Id>>map(z -> Either.left(HttpStatus.CONFLICT))
                .orElseGet(() -> this.saveAndReturnId(deviceDTO, id));
    }

    @Override
    public Either<HttpStatus, DeviceDTO> get(final Integer id) {
        final Optional<Device> device = this.deviceRepository.findById(id);

        return device.<Either<HttpStatus, DeviceDTO>>map(d -> Either.right(this.modelMapper.map(d, DeviceDTO.class)))
                .orElseGet(() -> Either.left(HttpStatus.NOT_FOUND));
    }

    @Override
    public void delete(final Integer id) {
        this.deviceRepository.deleteById(id);
    }

    private Either<HttpStatus, Id> saveAndReturnId(final MidDeviceDTO midDeviceDTO) {
        final Device device = this.modelMapper.map(midDeviceDTO, Device.class);

        final Integer id = this.deviceRepository.saveAndFlush(device).getId();

        return Either.right(new Id(id));
    }

    private Either<HttpStatus, Id> saveAndReturnId(final MidDeviceDTO midDeviceDTO, final Integer id) {
        final Device device = this.modelMapper.map(midDeviceDTO, Device.class);

        device.setId(id);

        this.deviceRepository.saveAndFlush(device);

        return Either.right(new Id(id));
    }
}
