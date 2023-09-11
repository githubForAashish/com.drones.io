package com.drones.io.payload;

import com.drones.io.enums.DroneModel;
import com.drones.io.enums.DroneState;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DroneDTO {
  private String serialNumber;
  private DroneModel model;
  private Integer weightLimit;
  private Integer batteryRemaining;
  private DroneState droneState;
}
