package com.drones.io.config;

import com.drones.io.entity.Drone;
import com.drones.io.enums.DroneModel;
import com.drones.io.repository.IDronesRepository;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="drone.setting")
@Getter
@Setter
public class DroneConfig {
    private Integer fleetCapacity;
    private Integer batteryThreshold;
    @Range(min=0, max=1)
    private Float decayRate;
    private Integer maximumWeightLimit;
}
