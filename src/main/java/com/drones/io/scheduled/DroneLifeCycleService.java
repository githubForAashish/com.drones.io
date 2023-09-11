package com.drones.io.scheduled;

import com.drones.io.entity.Drone;
import com.drones.io.enums.DroneState;
import com.drones.io.repository.IDronesRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Transactional
@Component
@RequiredArgsConstructor
public class DroneLifeCycleService {

  private static final Logger log = LoggerFactory.getLogger("DRONE_BATTERY_AUDITOR");

  /** Prepare Log from {@link Drone} */
  private static final Consumer<Drone> MakeLog =
      (Drone drone) -> {
        log.info(
            String.format(
                "|Drone: %1$10s|\t|Battery Left: %2$5s|\t|State: %3$10s|",
                drone.getSerialNumber(), drone.getBatteryRemaining(), drone.getState()));
      };

  private final IDronesRepository dronesRepository;

  @Value("${drone.setting.decay-rate}")
  private Float DECAY_RATE;

  /**
   * Scheduled task for simulation of drone activity. Facilitates state transitions. Also incurs
   * battery drain on every state transition.
   */
  @Scheduled(fixedRateString = "${scheduler.interval}", timeUnit = TimeUnit.SECONDS)
  public void simulateDroneActivity() {

    // Returning drones are now inactive and ready for loading.
    updateDroneState(DroneState.RETURNING, DroneState.IDLE);

    // Return home delivered drones.
    updateDroneState(DroneState.DELIVERED, DroneState.RETURNING);

    // End delivery for drones in "DELIVERING" state.
    updateDroneState(DroneState.DELIVERING, DroneState.DELIVERED);

    // Start Delivery of drones in "LOADED" or "LOADING" state.
    updateDroneState(DroneState.LOADING, DroneState.DELIVERING);
    updateDroneState(DroneState.LOADED, DroneState.DELIVERING);

    // Log Battery Information
    logBatteryInfo();
  }

  private void logBatteryInfo() {
    dronesRepository.findAll().forEach(MakeLog);
  }

  /**
   * Updates state of drones. A battery decay is incurred on every invocation using {@link
   * DroneLifeCycleService#DECAY_RATE}. If state transition from {@link DroneState#DELIVERING} ->
   * {@link DroneState#DELIVERED}, unload the drone.
   *
   * @param currentState Current State of Drones
   * @param nextState Next State of Drones
   */
  private void updateDroneState(DroneState currentState, DroneState nextState) {
    List<Drone> drones = dronesRepository.findAllByState(currentState);
    drones.forEach(
        drone -> {
          drone.setBatteryRemaining((int) (drone.getBatteryRemaining() * (1.0 - DECAY_RATE)));
          drone.setState(nextState);
          if (nextState.equals(DroneState.DELIVERED)) drone.unloadMedications();
        });
    dronesRepository.saveAll(drones);
  }
}
