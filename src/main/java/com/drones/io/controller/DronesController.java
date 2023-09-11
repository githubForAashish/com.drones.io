package com.drones.io.controller;

import com.drones.io.payload.DroneDTO;
import com.drones.io.payload.MedicationDTO;
import com.drones.io.payload.RegisterDroneRequestSchema;
import com.drones.io.service.DronesService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Null;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/drones")
@RestController
public class DronesController {

    private final DronesService droneService;

    @GetMapping("fetch-available-drones")
    public ResponseEntity<List<DroneDTO>> getDronesAvailableForLoading() {
        return ResponseEntity.ok(droneService.getDronesAvailableForLoading());
    }

    @GetMapping("battery-level/{droneSerialNumber}")
    public ResponseEntity<Integer> droneBatteryLevel(@PathVariable String droneSerialNumber) {
        return ResponseEntity.ok(droneService.getDroneBatteryLevel(droneSerialNumber));
    }

    @GetMapping("fetch-drone/{droneSerialNumber}")
    public ResponseEntity<DroneDTO> getDroneBySerialNumber(@PathVariable String droneSerialNumber) {
        return ResponseEntity.ok(droneService.getDroneBySerialNumber(droneSerialNumber));
    }

    @GetMapping("fetch-all-drones")
    public ResponseEntity<List<DroneDTO>> getAllDrones() {
        return ResponseEntity.ok(droneService.getAllDrones());
    }

    @GetMapping("fetch-loaded-medications/{droneSerialNumber}")
    public ResponseEntity<List<MedicationDTO>> getLoadedMedications(@PathVariable String droneSerialNumber) {
        return ResponseEntity.ok(droneService.getLoadedMedications(droneSerialNumber));
    }

    @GetMapping("charge-drone/{droneSerialNumber}")
    public ResponseEntity<Boolean> chargeDrone(@PathVariable String droneSerialNumber) {
        return ResponseEntity.ok(droneService.chargeDrone(droneSerialNumber));
    }

    @PostMapping("register-drone")
    public ResponseEntity<DroneDTO> registerDrone(@Valid @RequestBody final RegisterDroneRequestSchema drone) {
        return ResponseEntity.ok(droneService.registerDrone(drone));
    }

    @PostMapping("load-drone/{droneSerialNumber}")
    public ResponseEntity<DroneDTO> loadDrone(@PathVariable String droneSerialNumber, @Valid @RequestBody final List<MedicationDTO> medications) {
        return ResponseEntity.ok(droneService.loadMedications(droneSerialNumber, medications));
    }

}
