package com.drones.io.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = MaximumWeightCapacityValidator.class)
public @interface MaximumWeightCapacity {

        String message() default "must be less than or equal to %s";

        Class<?>[] groups() default { };

        Class<? extends Payload>[] payload() default { };
}
