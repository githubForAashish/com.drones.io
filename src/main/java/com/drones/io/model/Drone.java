package com.drones.io.model;


import com.drones.io.enums.DroneModel;
import com.drones.io.enums.DroneState;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name="DRONES")
public class Drone extends BaseEntity {

    @Size(min = 1, max = 100)
    @NotNull
    @Column(unique = true)
    private String serialNumber;

    @Enumerated(EnumType.STRING)
    @NotNull
    private DroneModel model;

    @Min(0)
    @Max(500)
    private Integer maximumWeightCapacity;

    @Min(0)
    @Max(100)
    @Value("${drone.setting.initial-battery}")
    @NotNull
    private Integer batteryRemaining;

    @Enumerated(EnumType.STRING)
    @NotNull
    private DroneState state;

    @JsonIgnore
    @OneToMany(mappedBy = "drone", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Medication> medications = new ArrayList<>();

    public void addMedications(List<Medication> medications) {
        this.medications.addAll(medications);
        medications.forEach((medication) -> medication.setDrone(this));
    }
}
