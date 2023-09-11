package com.drones.io.config;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/** Drone Specific Configurations Eg: Fleet Capacity, loading cut off threshold etc. */
@Configuration
@ConfigurationProperties(prefix = "drone.setting")
@Getter
@Setter
public class DroneConfig {
  private Integer fleetCapacity;
  private Integer batteryThreshold;

  @Range(min = 0, max = 1)
  private Float decayRate;

  private Integer maximumWeightLimit;
}
