package com.drones.io.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import java.net.URI;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "MEDICATIONS")
public class Medication extends BaseEntity {

  @Pattern(regexp = "[a-zA-Z_0-9-]+")
  private String name;

  @Pattern(regexp = "[A-Z0-9-]+")
  @Column(nullable = false)
  private String code;

  @Positive
  @Column(nullable = false)
  private Integer weight;

  @Column(name = "PICTURE_LOCATION")
  private URI pictureLocation;

  @JsonIgnore
  @ManyToOne(fetch = FetchType.LAZY)
  private Drone drone;
}
