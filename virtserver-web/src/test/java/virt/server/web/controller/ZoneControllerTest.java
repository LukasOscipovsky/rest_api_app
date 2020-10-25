package virt.server.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Either;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import virt.server.converter.ResponseConverter;
import virt.server.dto.DeviceDTO;
import virt.server.dto.Identifier;
import virt.server.dto.ZoneDTO;
import virt.server.dto.error.ApiError;
import virt.server.dto.error.ErrorDetails;
import virt.server.dto.error.Type;
import virt.server.service.impl.ZoneEntityService;

import java.util.Collections;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class ZoneControllerTest {

    private static final String ZONE_NAME = "zoneName";
    private static final String ZONE_DESCRIPTION = "zoneDesc";
    private static final Long ZONE_ID = 1L;
    private static final Long DEVICE_ID = 2L;
    private static final String DEVICE_ADDRESS = "127.0.0.1";
    private static final String DEVICE_DESCRIPTION = "deviceDesc";
    private static final String ZONES = "/zones";
    private static final String FIELD_NAME = "fieldName";
    private static final String MESSAGE = "message";

    @Mock
    private ZoneEntityService zoneEntityService;

    @Mock
    private ResponseConverter responseConverter;

    @InjectMocks
    private ZoneController zoneController;

    private ZoneDTO zoneDTO;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Identifier identifier;
    private ApiError apiError;

    @BeforeEach
    void setUp() {
        final ErrorDetails errorDetails = new ErrorDetails(FIELD_NAME, MESSAGE);

        this.apiError = new ApiError(Type.NOT_FOUND, Collections.singletonList(errorDetails));

        this.identifier = new Identifier(ZONE_ID);

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

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.zoneController)
                .setControllerAdvice(new ExceptionController())
                .build();

        this.objectMapper = new ObjectMapper();
    }

    @Test
    void getZone() throws Exception {
        when(this.zoneEntityService.get(ZONE_ID)).thenReturn(Either.right(this.zoneDTO));
        when(this.responseConverter.getResponse(Either.right(this.zoneDTO), HttpStatus.OK)).thenReturn(new ResponseEntity<>(this.zoneDTO, HttpStatus.OK));

        this.mockMvc.perform(get(ZONES + "/" + ZONE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(ZONE_ID.intValue())))
                .andExpect(jsonPath("$.name", Matchers.is(ZONE_NAME)))
                .andExpect(jsonPath("$.description", Matchers.is(ZONE_DESCRIPTION)));

        verify(this.zoneEntityService).get(ZONE_ID);
    }

    @Test
    void getZone_notFound() throws Exception {
        when(this.zoneEntityService.get(ZONE_ID)).thenReturn(Either.left(HttpStatus.NOT_FOUND));
        when(this.responseConverter.getResponse(Either.left(HttpStatus.NOT_FOUND), HttpStatus.OK)).thenReturn(new ResponseEntity<>(this.apiError, HttpStatus.NOT_FOUND));

        this.mockMvc.perform(get(ZONES + "/" + ZONE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(this.zoneEntityService).get(ZONE_ID);
    }

    @Test
    void getAllZones() throws Exception {
        when(this.zoneEntityService.getAll()).thenReturn(Collections.singletonList(this.zoneDTO));

        this.mockMvc.perform(get(ZONES)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", Matchers.is(ZONE_ID.intValue())))
                .andExpect(jsonPath("$.[0].name", Matchers.is(ZONE_NAME)))
                .andExpect(jsonPath("$.[0].description", Matchers.is(ZONE_DESCRIPTION)));

        verify(this.zoneEntityService).getAll();
    }

    @Test
    void createZone() throws Exception {
        final String serZone = this.objectMapper.writeValueAsString(this.zoneDTO);

        when(this.zoneEntityService.create(this.objectMapper.readValue(serZone, ZoneDTO.class))).thenReturn(this.identifier);

        this.mockMvc.perform(post(ZONES)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(serZone))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(ZONE_ID.intValue())));

        verify(this.zoneEntityService).create(this.objectMapper.readValue(serZone, ZoneDTO.class));
    }

    @Test
    void updateZone() throws Exception {
        final String serZone = this.objectMapper.writeValueAsString(this.zoneDTO);

        when(this.zoneEntityService.update(this.objectMapper.readValue(serZone, ZoneDTO.class), ZONE_ID)).thenReturn(Either.right(this.identifier));
        when(this.responseConverter.getResponse(Either.right(this.identifier), HttpStatus.OK)).thenReturn(new ResponseEntity<>(this.identifier, HttpStatus.OK));

        this.mockMvc.perform(put(ZONES + "/" + ZONE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(serZone))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(ZONE_ID.intValue())));

        verify(this.zoneEntityService).update(this.objectMapper.readValue(serZone, ZoneDTO.class), ZONE_ID);
    }

    @Test
    void updateZone_notFound() throws Exception {
        final String serZone = this.objectMapper.writeValueAsString(this.zoneDTO);

        when(this.zoneEntityService.update(this.objectMapper.readValue(serZone, ZoneDTO.class), ZONE_ID)).thenReturn(Either.left(HttpStatus.NOT_FOUND));
        when(this.responseConverter.getResponse(Either.left(HttpStatus.NOT_FOUND), HttpStatus.OK)).thenReturn(new ResponseEntity<>(this.apiError, HttpStatus.NOT_FOUND));

        this.mockMvc.perform(put(ZONES + "/" + ZONE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(serZone))
                .andExpect(status().isNotFound());

        verify(this.zoneEntityService).update(this.objectMapper.readValue(serZone, ZoneDTO.class), ZONE_ID);
    }

    @Test
    void deleteZone() throws Exception {
        when(this.zoneEntityService.delete(ZONE_ID)).thenReturn(Either.right(this.identifier));
        when(this.responseConverter.getResponse(Either.right(this.identifier), HttpStatus.OK)).thenReturn(new ResponseEntity<>(this.identifier, HttpStatus.OK));

        this.mockMvc.perform(delete(ZONES + "/" + ZONE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(ZONE_ID.intValue())));

        verify(this.zoneEntityService).delete(ZONE_ID);
    }

    @Test
    void deleteZone_notFound() throws Exception {
        when(this.zoneEntityService.delete(ZONE_ID)).thenReturn(Either.left(HttpStatus.NOT_FOUND));
        when(this.responseConverter.getResponse(Either.left(HttpStatus.NOT_FOUND), HttpStatus.OK)).thenReturn(new ResponseEntity<>(this.apiError, HttpStatus.NOT_FOUND));

        this.mockMvc.perform(delete(ZONES + "/" + ZONE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(this.zoneEntityService).delete(ZONE_ID);
    }
}