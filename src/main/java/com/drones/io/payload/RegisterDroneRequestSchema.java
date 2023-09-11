package com.drones.io.payload;

import com.drones.io.enums.DroneModel;
import com.drones.io.validator.MaximumWeightCapacity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDroneRequestSchema {

  @Size(min = 1, max = 100, message = "must be between 1 and 100 characters")
  @NotBlank(message = "Must provide value for this field")
  private String serialNumber;

  @Enumerated(EnumType.STRING)
  @NotNull(message = "Must provide a drone model.")
  private DroneModel model;

  @MaximumWeightCapacity
  @NotNull(message = "Must provide medication carrying capacity.")
  private Integer weightLimit;
}
