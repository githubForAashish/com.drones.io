package com.drones.io.controller;

import com.drones.io.service.DronesService;
import com.drones.io.model.Drone;
import com.drones.io.model.Medication;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/drones")
@RestController
public class DronesController {
    private final DronesService droneService;

    @GetMapping("is-alive")
    public ResponseEntity<Boolean> isAlive() {
        return ResponseEntity.ok(Boolean.TRUE);
    }

    @GetMapping("loaded-medications/{droneSerialNumber}")
    public ResponseEntity<List<Medication>> getLoadedMedications(@PathVariable String droneSerialNumber) {
        return ResponseEntity.ok(droneService.getLoadedMedications(droneSerialNumber));
    }

    @GetMapping("available-drones")
    public ResponseEntity<List<Drone>> getDronesAvailableForLoading() {
        return ResponseEntity.ok(droneService.getDronesAvailableForLoading());
    }

    @PostMapping("register-drone")
    public ResponseEntity<Drone> registerDrone(@Valid @RequestBody final Drone drone) {
        return ResponseEntity.ok(droneService.registerDrone(drone));
    }

    @PostMapping("load-drone/{droneSerialNumber}")
    public ResponseEntity<Drone> loadDrone(@PathVariable String droneSerialNumber, @Valid @RequestBody List<Medication> medications) {
        return ResponseEntity.ok(droneService.loadMedications(droneSerialNumber, medications));
    }




}
