package com.drones.io.entity;


import com.drones.io.enums.DroneModel;
import com.drones.io.enums.DroneState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "DRONES")
public class Drone extends BaseEntity {

    @Length(min = 1, max = 100)
    @Column(nullable = false, unique = true, name = "SERIAL_NUMBER")
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneModel model;

    @Min(0)
    @Column(nullable = false, name = "WEIGHT_LIMIT")
    private Integer weightLimit;

    @Min(0)
    @Max(100)
    @Column(nullable = false, name = "BATTERY_REMAINING")
    private Integer batteryRemaining = 100;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DroneState state = DroneState.IDLE;

    @JsonIgnore
    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Medication> medications = new ArrayList<>();

    public void addMedications(List<Medication> medications) {
        this.medications.addAll(medications);
        medications.forEach(medication -> medication.setDrone(this));
    }

    public void unloadMedications() {
        this.medications.forEach(medication -> medication.setDrone(null));
        this.medications.clear();
    }
}
