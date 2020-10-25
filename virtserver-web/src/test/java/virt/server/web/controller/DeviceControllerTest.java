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
import virt.server.dto.error.ApiError;
import virt.server.dto.error.ErrorDetails;
import virt.server.dto.error.Type;
import virt.server.service.impl.DeviceEntityService;

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
class DeviceControllerTest {
    private static final Long DEVICE_ID = 1L;
    private static final Long ZONE_ID = 2L;
    private static final String DEVICE_ADDRESS = "127.0.0.1";
    private static final String DEVICE_DESCRIPTION = "deviceDesc";
    private static final String DEVICES = "/devices";
    private static final String FIELD_NAME = "fieldName";
    private static final String MESSAGE = "message";

    @Mock
    private DeviceEntityService deviceEntityService;

    @Mock
    private ResponseConverter responseConverter;

    @InjectMocks
    private DeviceController deviceController;

    private DeviceDTO deviceDTO;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper;
    private Identifier identifier;
    private ApiError apiError;

    @BeforeEach
    void setUp() {
        final ErrorDetails errorDetails = new ErrorDetails(FIELD_NAME, MESSAGE);

        this.apiError = new ApiError(Type.NOT_FOUND, Collections.singletonList(errorDetails));

        this.identifier = new Identifier(DEVICE_ID);

        this.deviceDTO = new DeviceDTO();
        this.deviceDTO.setId(DEVICE_ID);
        this.deviceDTO.setZoneId(ZONE_ID);
        this.deviceDTO.setAddress(DEVICE_ADDRESS);
        this.deviceDTO.setDescription(DEVICE_DESCRIPTION);

        this.mockMvc = MockMvcBuilders.standaloneSetup(this.deviceController)
                .setControllerAdvice(new ExceptionController())
                .build();

        this.objectMapper = new ObjectMapper();
    }

    @Test
    void getDevice() throws Exception {
        when(this.deviceEntityService.get(DEVICE_ID)).thenReturn(Either.right(this.deviceDTO));
        when(this.responseConverter.getResponse(Either.right(this.deviceDTO), HttpStatus.OK)).thenReturn(new ResponseEntity<>(this.deviceDTO, HttpStatus.OK));

        this.mockMvc.perform(get(DEVICES + "/" + DEVICE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(DEVICE_ID.intValue())))
                .andExpect(jsonPath("$.address", Matchers.is(DEVICE_ADDRESS)))
                .andExpect(jsonPath("$.description", Matchers.is(DEVICE_DESCRIPTION)));

        verify(this.deviceEntityService).get(DEVICE_ID);
    }

    @Test
    void getDevice_notFound() throws Exception {
        when(this.deviceEntityService.get(DEVICE_ID)).thenReturn(Either.left(HttpStatus.NOT_FOUND));
        when(this.responseConverter.getResponse(Either.left(HttpStatus.NOT_FOUND), HttpStatus.OK)).thenReturn(new ResponseEntity<>(this.apiError, HttpStatus.NOT_FOUND));

        this.mockMvc.perform(get(DEVICES + "/" + DEVICE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(this.deviceEntityService).get(DEVICE_ID);
    }

    @Test
    void getAllDEVICES() throws Exception {
        when(this.deviceEntityService.getAll()).thenReturn(Collections.singletonList(this.deviceDTO));

        this.mockMvc.perform(get(DEVICES)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id", Matchers.is(DEVICE_ID.intValue())))
                .andExpect(jsonPath("$.[0].address", Matchers.is(DEVICE_ADDRESS)))
                .andExpect(jsonPath("$.[0].description", Matchers.is(DEVICE_DESCRIPTION)));

        verify(this.deviceEntityService).getAll();
    }

    @Test
    void createDevice() throws Exception {
        final String serDevice = this.objectMapper.writeValueAsString(this.deviceDTO);

        when(this.deviceEntityService.create(this.objectMapper.readValue(serDevice, DeviceDTO.class))).thenReturn(this.identifier);

        this.mockMvc.perform(post(DEVICES)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(serDevice))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", Matchers.is(DEVICE_ID.intValue())));

        verify(this.deviceEntityService).create(this.objectMapper.readValue(serDevice, DeviceDTO.class));
    }

    @Test
    void updateDevice() throws Exception {
        final String serDevice = this.objectMapper.writeValueAsString(this.deviceDTO);

        when(this.deviceEntityService.update(this.objectMapper.readValue(serDevice, DeviceDTO.class), DEVICE_ID)).thenReturn(Either.right(this.identifier));
        when(this.responseConverter.getResponse(Either.right(this.identifier), HttpStatus.OK)).thenReturn(new ResponseEntity<>(this.identifier, HttpStatus.OK));

        this.mockMvc.perform(put(DEVICES + "/" + DEVICE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(serDevice))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(DEVICE_ID.intValue())));

        verify(this.deviceEntityService).update(this.objectMapper.readValue(serDevice, DeviceDTO.class), DEVICE_ID);
    }

    @Test
    void updateDevice_notFound() throws Exception {
        final String serDevice = this.objectMapper.writeValueAsString(this.deviceDTO);

        when(this.deviceEntityService.update(this.objectMapper.readValue(serDevice, DeviceDTO.class), DEVICE_ID)).thenReturn(Either.left(HttpStatus.NOT_FOUND));
        when(this.responseConverter.getResponse(Either.left(HttpStatus.NOT_FOUND), HttpStatus.OK)).thenReturn(new ResponseEntity<>(this.apiError, HttpStatus.NOT_FOUND));

        this.mockMvc.perform(put(DEVICES + "/" + DEVICE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(serDevice))
                .andExpect(status().isNotFound());

        verify(this.deviceEntityService).update(this.objectMapper.readValue(serDevice, DeviceDTO.class), DEVICE_ID);
    }

    @Test
    void deleteDevice() throws Exception {
        when(this.deviceEntityService.delete(DEVICE_ID)).thenReturn(Either.right(this.identifier));
        when(this.responseConverter.getResponse(Either.right(this.identifier), HttpStatus.OK)).thenReturn(new ResponseEntity<>(this.identifier, HttpStatus.OK));

        this.mockMvc.perform(delete(DEVICES + "/" + DEVICE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", Matchers.is(DEVICE_ID.intValue())));

        verify(this.deviceEntityService).delete(DEVICE_ID);
    }

    @Test
    void deleteDevice_notFound() throws Exception {
        when(this.deviceEntityService.delete(DEVICE_ID)).thenReturn(Either.left(HttpStatus.NOT_FOUND));
        when(this.responseConverter.getResponse(Either.left(HttpStatus.NOT_FOUND), HttpStatus.OK)).thenReturn(new ResponseEntity<>(this.apiError, HttpStatus.NOT_FOUND));

        this.mockMvc.perform(delete(DEVICES + "/" + DEVICE_ID)
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());

        verify(this.deviceEntityService).delete(DEVICE_ID);
    }
}
