package com.drones.io.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@ConditionalOnProperty(prefix = "scheduler", name = "enable")
@Configuration
@ConfigurationProperties(prefix = "scheduler")
@Getter
@Setter
public class SchedulerConfig {
    private Boolean enable;
    private Integer interval;
}
