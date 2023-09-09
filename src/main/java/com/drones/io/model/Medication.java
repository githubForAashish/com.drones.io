package com.drones.io.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter
@Setter
@Entity
@Table(name="MEDICATIONS")
public class Medication extends BaseEntity{

    @Pattern(regexp = "[a-zA-Z_0-9-]+")
    @NotNull
    private String name;

    @Pattern(regexp = "[A-Z0-9-]+")
    @NotNull
    private String code;


    @Positive
    @NotNull
    private Integer weight;

    @NotNull
    private URL pictureLocation;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private Drone drone;

}
