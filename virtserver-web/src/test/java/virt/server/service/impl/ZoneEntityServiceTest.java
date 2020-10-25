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
import virt.server.data.api.repository.impl.ZoneDaoImpl;
import virt.server.dto.DeviceDTO;
import virt.server.dto.Identifier;
import virt.server.dto.ZoneDTO;
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
class ZoneEntityServiceTest {

    private static final String ZONE_NAME = "zoneName";
    private static final String ZONE_DESCRIPTION = "zoneDesc";
    private static final Long ZONE_ID = 1L;
    private static final Long DEVICE_ID = 2L;
    private static final String DEVICE_ADDRESS = "127.0.0.1";
    private static final String DEVICE_DESCRIPTION = "deviceDesc";

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ZoneDaoImpl zoneDao;

    @InjectMocks
    private ZoneEntityService zoneEntityService;

    private ZoneDTO zoneDTO;
    private Identifier identifier;
    private Zone zone;

    @BeforeEach
    void setUp() {
        this.identifier = new Identifier(ZONE_ID);

        final Device device = new Device();
        device.setId(DEVICE_ID);
        device.setAddress(DEVICE_ADDRESS);
        device.setDescription(DEVICE_DESCRIPTION);
        device.setZone(new Zone());

        this.zone = new Zone();
        this.zone.setName(ZONE_NAME);
        this.zone.setDescription(ZONE_DESCRIPTION);
        this.zone.setId(ZONE_ID);
        this.zone.setDevices(Collections.singletonList(device));

        final DeviceDTO deviceDTO = new DeviceDTO();
        deviceDTO.setId(DEVICE_ID);
        deviceDTO.setZoneId(ZONE_ID);
        deviceDTO.setAddress(DEVICE_ADDRESS);
        deviceDTO.setDescription(DEVICE_DESCRIPTION);

        this.zoneDTO = new ZoneDTO();
        this.zoneDTO.setName(ZONE_NAME);
        this.zoneDTO.setDescription(ZONE_DESCRIPTION);
        this.zoneDTO.setId(ZONE_ID);
        this.zoneDTO.setDevices(Collections.singletonList(deviceDTO));
    }

    @Test
    void testCreate() {
        when(this.modelMapper.map(this.zoneDTO, Zone.class)).thenReturn(this.zone);

        final Identifier identifier = this.zoneEntityService.create(this.zoneDTO);

        assertThat(identifier).isEqualTo(this.identifier);

        verify(this.zoneDao).save(this.zone);
        verify(this.modelMapper).map(this.zoneDTO, Zone.class);
    }

    @Test
    void testUpdate() {
        when(this.modelMapper.map(this.zoneDTO, Zone.class)).thenReturn(this.zone);
        when(this.zoneDao.update(this.zone)).thenReturn(Optional.of(this.zone));

        final Either<HttpStatus, Identifier> either = this.zoneEntityService.update(this.zoneDTO, ZONE_ID);

        assertThat(either.isRight()).isTrue();

        verify(this.zoneDao).update(this.zone);
        verify(this.modelMapper).map(this.zoneDTO, Zone.class);
    }

    @Test
    void testUpdate_notFound() {
        when(this.modelMapper.map(this.zoneDTO, Zone.class)).thenReturn(this.zone);
        when(this.zoneDao.update(this.zone)).thenReturn(Optional.empty());

        final Either<HttpStatus, Identifier> either = this.zoneEntityService.update(this.zoneDTO, ZONE_ID);

        assertThat(either.isRight()).isFalse();

        verify(this.zoneDao).update(this.zone);
        verify(this.modelMapper).map(this.zoneDTO, Zone.class);
    }

    @Test
    void testGet() {
        when(this.modelMapper.map(this.zone, ZoneDTO.class)).thenReturn(this.zoneDTO);
        when(this.zoneDao.get(ZONE_ID)).thenReturn(Optional.of(this.zone));

        final Either<HttpStatus, ZoneDTO> either = this.zoneEntityService.get(ZONE_ID);

        assertThat(either.isRight()).isTrue();
        assertThat(either.get()).isEqualTo(this.zoneDTO);

        verify(this.zoneDao).get(ZONE_ID);
        verify(this.modelMapper).map(this.zone, ZoneDTO.class);
    }

    @Test
    void testGet_notFound() {
        when(this.zoneDao.get(ZONE_ID)).thenReturn(Optional.empty());

        final Either<HttpStatus, ZoneDTO> either = this.zoneEntityService.get(ZONE_ID);

        assertThat(either.isRight()).isFalse();

        verify(this.zoneDao).get(ZONE_ID);
        verify(this.modelMapper, never()).map(this.zone, ZoneDTO.class);
    }

    @Test
    void testGetAll() {
        when(this.modelMapper.map(this.zone, ZoneDTO.class)).thenReturn(this.zoneDTO);
        when(this.zoneDao.getAll()).thenReturn(Collections.singletonList(this.zone));

        final List<ZoneDTO> list = this.zoneEntityService.getAll();

        assertThat(list.size()).isEqualTo(1);
        assertThat(list.get(0)).isEqualTo(this.zoneDTO);

        verify(this.modelMapper).map(this.zone, ZoneDTO.class);
    }

    @Test
    void testDelete() {
        when(this.zoneDao.delete(ZONE_ID)).thenReturn(true);

        final Either<HttpStatus, Identifier> either = this.zoneEntityService.delete(ZONE_ID);

        assertThat(either.isRight()).isTrue();
        assertThat(either.get()).isEqualTo(this.identifier);

        verify(this.zoneDao).delete(ZONE_ID);
    }
}