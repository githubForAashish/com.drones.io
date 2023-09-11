package com.drones.io.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.net.URI;

@Getter
@Setter
public class MedicationDTO {
    @Pattern(regexp = "[a-zA-Z_0-9-]+", message = "name must consist of alphanumeric characters and hyphen('-') and underscore ('_').")
    private String name;

    @Pattern(regexp = "[A-Z0-9-]+", message = "code must consists of upper case alphanumeric character and hyphen('-')")
    @NotBlank(message = "must provide medication code.")
    private String code;

    @Positive(message = "weight of medication must be positive.")
    @NotNull(message = "weight of medication must be provided.")
    private Integer weight;

    private URI pictureLocation;
}
