package virt.server.service.impl;

import io.vavr.control.Either;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import virt.server.data.api.repository.impl.DeviceDaoImpl;
import virt.server.dto.DeviceDTO;
import virt.server.dto.Identifier;
import virt.server.entities.Device;
import virt.server.entities.Zone;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeviceEntityServiceTest {
    private static final Long ZONE_ID = 1L;
    private static final Long DEVICE_ID = 2L;
    private static final String DEVICE_ADDRESS = "127.0.0.1";
    private static final String DEVICE_DESCRIPTION = "deviceDesc";

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private DeviceDaoImpl deviceDao;

    @InjectMocks
    private DeviceEntityService deviceEntityService;

    private DeviceDTO deviceDTO;
    private Device device;
    private Identifier identifier;

    @BeforeEach
    void setUp() {
        this.identifier = new Identifier(DEVICE_ID);

        this.device = new Device();
        this.device.setId(DEVICE_ID);
        this.device.setAddress(DEVICE_ADDRESS);
        this.device.setDescription(DEVICE_DESCRIPTION);
        this.device.setZone(new Zone());

        this.deviceDTO = new DeviceDTO();
        this.deviceDTO.setId(DEVICE_ID);
        this.deviceDTO.setZoneId(ZONE_ID);
        this.deviceDTO.setAddress(DEVICE_ADDRESS);
        this.deviceDTO.setDescription(DEVICE_DESCRIPTION);
    }

    @Test
    void testCreate() {
        when(this.modelMapper.map(this.deviceDTO, Device.class)).thenReturn(this.device);

        final Identifier identifier = this.deviceEntityService.create(this.deviceDTO);

        assertThat(identifier).isEqualTo(this.identifier);

        verify(this.deviceDao).save(this.device);
        verify(this.modelMapper).map(this.deviceDTO, Device.class);
    }

    @Test
    void testUpdate() {
        when(this.modelMapper.map(this.deviceDTO, Device.class)).thenReturn(this.device);
        when(this.deviceDao.update(this.device)).thenReturn(Optional.of(this.device));

        final Either<HttpStatus, Identifier> either = this.deviceEntityService.update(this.deviceDTO, DEVICE_ID);

        assertThat(either.isRight()).isTrue();

        verify(this.deviceDao).update(this.device);
        verify(this.modelMapper).map(this.deviceDTO, Device.class);
    }

    @Test
    void testUpdate_notFound() {
        when(this.modelMapper.map(this.deviceDTO, Device.class)).thenReturn(this.device);
        when(this.deviceDao.update(this.device)).thenReturn(Optional.empty());

        final Either<HttpStatus, Identifier> either = this.deviceEntityService.update(this.deviceDTO, DEVICE_ID);

        assertThat(either.isRight()).isFalse();

        verify(this.deviceDao).update(this.device);
        verify(this.modelMapper).map(this.deviceDTO, Device.class);
    }

    @Test
    void testGet() {
        when(this.modelMapper.map(this.device, DeviceDTO.class)).thenReturn(this.deviceDTO);
        when(this.deviceDao.get(DEVICE_ID)).thenReturn(Optional.of(this.device));

        final Either<HttpStatus, DeviceDTO> either = this.deviceEntityService.get(DEVICE_ID);

        assertThat(either.isRight()).isTrue();
        assertThat(either.get()).isEqualTo(this.deviceDTO);

        verify(this.deviceDao).get(DEVICE_ID);
        verify(this.modelMapper).map(this.device, DeviceDTO.class);
    }

    @Test
    void testGet_notFound() {
        when(this.deviceDao.get(DEVICE_ID)).thenReturn(Optional.empty());

        final Either<HttpStatus, DeviceDTO> either = this.deviceEntityService.get(DEVICE_ID);

        assertThat(either.isRight()).isFalse();

        verify(this.deviceDao).get(DEVICE_ID);
        verify(this.modelMapper, never()).map(this.device, DeviceDTO.class);
    }

    @Test
    void testGetAll() {
        when(this.modelMapper.map(this.device, DeviceDTO.class)).thenReturn(this.deviceDTO);
        when(this.deviceDao.getAll()).thenReturn(Collections.singletonList(this.device));

        final List<DeviceDTO> list = this.deviceEntityService.getAll();

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0)).isEqualTo(this.deviceDTO);

        verify(this.modelMapper).map(this.device, DeviceDTO.class);
    }

    @Test
    void testDelete() {
        when(this.deviceDao.delete(DEVICE_ID)).thenReturn(true);

        final Either<HttpStatus, Identifier> either = this.deviceEntityService.delete(DEVICE_ID);

        assertThat(either.isRight()).isTrue();
        assertThat(either.get()).isEqualTo(this.identifier);

        verify(this.deviceDao).delete(DEVICE_ID);
    }
}
