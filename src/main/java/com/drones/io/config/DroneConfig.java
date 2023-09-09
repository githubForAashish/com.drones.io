package com.drones.io.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix="drone.setting")
@Getter
@Setter
public class DroneConfig {
    private Integer fleet;
    private Integer batteryThreshold;
}
