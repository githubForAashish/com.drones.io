package com.drones.io.service;

import static com.drones.io.exception.ApplicationExceptionCodes.*;

import com.drones.io.config.DroneConfig;
import com.drones.io.entity.Drone;
import com.drones.io.entity.Medication;
import com.drones.io.enums.DroneState;
import com.drones.io.exception.ApplicationException;
import com.drones.io.payload.DroneDTO;
import com.drones.io.payload.MedicationDTO;
import com.drones.io.payload.RegisterDroneRequestSchema;
import com.drones.io.repository.IDronesRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class DronesService {
  private final IDronesRepository dronesRepository;
  private final ModelMapper modelMapper;
  private final DroneConfig droneConfig;

  public DroneDTO registerDrone(RegisterDroneRequestSchema drone) {
    Drone newDrone = modelMapper.map(drone, Drone.class);
    if (dronesRepository.count() >= droneConfig.getFleetCapacity())
      throw ApplicationException.create(FLEET_CAPACITY_REACHED);
    if (dronesRepository.findBySerialNumber(drone.getSerialNumber()).isPresent())
      throw ApplicationException.create(DRONE_ALREADY_REGISTERED);
    return modelMapper.map(dronesRepository.save(newDrone), DroneDTO.class);
  }

  public List<MedicationDTO> getLoadedMedications(String droneSerialNumber) {
    Drone drone =
        dronesRepository
            .findBySerialNumber(droneSerialNumber)
            .orElseThrow(() -> ApplicationException.create(DRONE_NOT_FOUND));
    return drone.getMedications().stream()
        .map(medication -> modelMapper.map(medication, MedicationDTO.class))
        .collect(Collectors.toList());
  }

  public DroneDTO getDroneBySerialNumber(String droneSerialNumber) {
    return modelMapper.map(
        dronesRepository
            .findBySerialNumber(droneSerialNumber)
            .orElseThrow(() -> ApplicationException.create(DRONE_NOT_FOUND)),
        DroneDTO.class);
  }

  public List<DroneDTO> getAllDrones() {
    List<DroneDTO> drones = new ArrayList<>();
    dronesRepository.findAll().forEach(drone -> drones.add(modelMapper.map(drone, DroneDTO.class)));
    return drones;
  }

  /*
   * To be able to load drone with medication:
   * Weight Limit must not be reached
   * Battery > THRESHOLD% (defined in Application Properties)
   * Should be in IDLE or LOADING state;
   */
  public DroneDTO loadMedications(String droneSerialNumber, List<MedicationDTO> medications) {

    List<Medication> newMedications =
        medications.stream()
            .map(medicationDTO -> modelMapper.map(medicationDTO, Medication.class))
            .collect(Collectors.toList());

    Drone drone =
        dronesRepository
            .findBySerialNumber(droneSerialNumber)
            .orElseThrow(() -> ApplicationException.create(DRONE_NOT_FOUND));

    if (!(drone.getState().equals(DroneState.IDLE) || drone.getState().equals(DroneState.LOADING)))
      throw ApplicationException.create(DRONE_PREOCCUPIED);
    if (drone.getBatteryRemaining() <= droneConfig.getBatteryThreshold())
      throw ApplicationException.create(DRONE_BATTERY_TOO_LOW);

    int totalNewMedicationsWeight = newMedications.stream().mapToInt(Medication::getWeight).sum();
    int existingMedicationsWeight =
        drone.getMedications().stream().mapToInt(Medication::getWeight).sum();
    if (existingMedicationsWeight + totalNewMedicationsWeight < drone.getWeightLimit())
      drone.setState(DroneState.LOADING);
    else if (drone.getWeightLimit().equals(totalNewMedicationsWeight + existingMedicationsWeight))
      drone.setState(DroneState.LOADED);
    else throw ApplicationException.create(DRONE_OVERLOADED);

    drone.addMedications(newMedications);
    dronesRepository.save(drone);
    return modelMapper.map(drone, DroneDTO.class);
  }

  public List<DroneDTO> getDronesAvailableForLoading() {
    return dronesRepository
        .listDronesAvailableForLoading(droneConfig.getBatteryThreshold())
        .stream()
        .map(drone -> modelMapper.map(drone, DroneDTO.class))
        .collect(Collectors.toList());
  }

  public Integer getDroneBatteryLevel(String droneSerialNumber) {
    Drone drone =
        dronesRepository
            .findBySerialNumber(droneSerialNumber)
            .orElseThrow(() -> ApplicationException.create(DRONE_NOT_FOUND));
    return drone.getBatteryRemaining();
  }

  /** Assume instantaneous charge. */
  public Boolean chargeDrone(String droneSerialNumber) {
    Drone drone =
        dronesRepository
            .findBySerialNumber(droneSerialNumber)
            .orElseThrow(() -> ApplicationException.create(DRONE_NOT_FOUND));
    drone.setBatteryRemaining(100);
    dronesRepository.save(drone);
    return Boolean.TRUE;
  }
}
