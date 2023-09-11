package com.drones.io.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.drones.io.config.DroneConfig;
import com.drones.io.entity.Drone;
import com.drones.io.enums.DroneModel;
import com.drones.io.enums.DroneState;
import com.drones.io.exception.ApplicationException;
import com.drones.io.payload.DroneDTO;
import com.drones.io.payload.RegisterDroneRequestSchema;
import com.drones.io.repository.IDronesRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
public class DroneServiceTest {

  private static final String TEST_DRONE_SN = "test-drone-1";
  @Mock private IDronesRepository dronesRepository;
  @Mock private ModelMapper modelMapper;
  @Mock private DroneConfig droneConfig;

  @InjectMocks private DronesService dronesService;
  private Drone drone;
  private RegisterDroneRequestSchema registerDroneRequest;
  private DroneDTO droneDTO;

  @BeforeEach
  public void setup() {
    drone =
        Drone.builder()
            .serialNumber(TEST_DRONE_SN)
            .state(DroneState.IDLE)
            .weightLimit(100)
            .model(DroneModel.CRUISERWEIGHT)
            .batteryRemaining(100)
            .build();

    registerDroneRequest =
        RegisterDroneRequestSchema.builder()
            .serialNumber(TEST_DRONE_SN)
            .model(DroneModel.CRUISERWEIGHT)
            .weightLimit(100)
            .build();

    droneDTO =
        DroneDTO.builder()
            .serialNumber(TEST_DRONE_SN)
            .model(DroneModel.CRUISERWEIGHT)
            .weightLimit(100)
            .batteryRemaining(100)
            .droneState(DroneState.IDLE)
            .build();
  }

  @DisplayName("Register Drone Successfully")
  @Test
  public void givenDTO_whenRegisterDrone_thenReturnDroneDTO() {
    // Arrange
    given(modelMapper.map(registerDroneRequest, Drone.class)).willReturn(drone);
    given(droneConfig.getFleetCapacity()).willReturn(10);
    given(dronesRepository.count()).willReturn(0L);
    given(dronesRepository.findBySerialNumber(registerDroneRequest.getSerialNumber()))
        .willReturn(Optional.empty());
    given(dronesRepository.save(drone)).willReturn(drone);
    given(modelMapper.map(drone, DroneDTO.class)).willReturn(droneDTO);

    // Act
    DroneDTO drone = dronesService.registerDrone(registerDroneRequest);

    // Assert
    assertThat(drone).isNotNull();
  }

  @DisplayName("Drone with SerialNumber exists")
  @Test
  public void givenDTO_whenRegisterDrone_thenThrowDroneExistsException() {
    // Arrange
    given(modelMapper.map(registerDroneRequest, Drone.class)).willReturn(drone);
    given(droneConfig.getFleetCapacity()).willReturn(10);
    given(dronesRepository.count()).willReturn(1L);
    given(dronesRepository.findBySerialNumber(registerDroneRequest.getSerialNumber()))
        .willReturn(Optional.of(drone));

    // Act
    assertThrows(
        ApplicationException.class, () -> dronesService.registerDrone(registerDroneRequest));

    // Assert
    verify(dronesRepository, never()).save(any(Drone.class));
  }
}
