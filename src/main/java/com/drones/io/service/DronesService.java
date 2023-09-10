package com.drones.io.service;

import com.drones.io.enums.DroneState;
import com.drones.io.exception.ApplicationException;
import com.drones.io.model.Drone;
import com.drones.io.model.Medication;
import com.drones.io.repository.IDronesRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.drones.io.exception.ApplicationExceptionCodes.*;


@RequiredArgsConstructor
@Transactional
@Service
public class DronesService {
    @Value("${drone.setting.battery-threshold}")
    private Integer BATTERY_THRESHOLD;

    @Value("${drone.setting.fleet-capacity}")
    private Integer FLEET_CAPACITY;

    private final IDronesRepository dronesRepository;

    public Drone registerDrone(Drone drone) {
        if (dronesRepository.count() >= FLEET_CAPACITY)
            throw ApplicationException.create(FLEET_CAPACITY_REACHED);
        return dronesRepository.save(drone);
    }

    public List<Medication> getLoadedMedications(String droneSerialNumber) {
        Drone drone = dronesRepository.findBySerialNumber(droneSerialNumber).orElseThrow(() -> ApplicationException.create(DRONE_NOT_FOUND));
        return drone.getMedications();
    }

    /*
     * To be able to load drone with medication:
     * Weight Limit must not be reached
     * Battery > THRESHOLD% (defined in Application Properties)
     * Should be in IDLE or LOADING state;
     */
    public Drone loadMedications(String droneSerialNumber, List<Medication> newMedications) {
        Drone drone = dronesRepository.findBySerialNumber(droneSerialNumber).orElseThrow(() -> ApplicationException.create(DRONE_NOT_FOUND));

        if (!(drone.getState().equals(DroneState.IDLE) || drone.getState().equals(DroneState.LOADING)))
            throw ApplicationException.create(DRONE_PREOCCUPIED);
        if (drone.getBatteryRemaining() <= BATTERY_THRESHOLD)
            throw ApplicationException.create(DRONE_BATTERY_TOO_LOW);

        int totalNewMedicationsWeight = newMedications.stream().mapToInt(Medication::getWeight).sum();
        int existingMedicationsWeight = drone.getMedications().stream().mapToInt(Medication::getWeight).sum();
        if (existingMedicationsWeight + totalNewMedicationsWeight < drone.getMaximumWeightCapacity())
            drone.setState(DroneState.LOADING);
        else if (drone.getMaximumWeightCapacity().equals(totalNewMedicationsWeight + existingMedicationsWeight))
            drone.setState(DroneState.LOADED);
        else throw ApplicationException.create(DRONE_OVERLOADED);

        drone.addMedications(newMedications);
        dronesRepository.save(drone);
        return drone;
    }

    public List<Drone> getDronesAvailableForLoading() {
        return dronesRepository.listDronesAvailableForLoading(BATTERY_THRESHOLD);
    }

    public Integer getDroneBatteryLevel(String droneSerialNumber) {
        Drone drone = dronesRepository.findBySerialNumber(droneSerialNumber).orElseThrow(() -> ApplicationException.create(DRONE_NOT_FOUND));
        return drone.getBatteryRemaining();
    }

}
