package com.drones.io.payload;

import com.drones.io.enums.DroneModel;
import com.drones.io.enums.DroneState;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DroneDTO {
  private String serialNumber;
  private DroneModel model;
  private Integer weightLimit;
  private Integer batteryRemaining;
  private DroneState droneState;
}
