package com.drones.io.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Value;

public class MaximumWeightCapacityValidator
    implements ConstraintValidator<MaximumWeightCapacity, Integer> {

  @Value("${drone.setting.maximum-weight-limit}")
  private int MAX_WEIGHT_LIMIT;

  private void formatMessage(ConstraintValidatorContext context) {
    String msg = context.getDefaultConstraintMessageTemplate();
    String formattedMsg = String.format(msg, this.MAX_WEIGHT_LIMIT);
    context.disableDefaultConstraintViolation();
    context.buildConstraintViolationWithTemplate(formattedMsg).addConstraintViolation();
  }

  @Override
  public void initialize(MaximumWeightCapacity constraintAnnotation) {
    ConstraintValidator.super.initialize(constraintAnnotation);
  }

  @Override
  public boolean isValid(Integer value, ConstraintValidatorContext constraintValidatorContext) {
    if (value == null) return true;
    formatMessage(constraintValidatorContext);
    return value <= MAX_WEIGHT_LIMIT;
  }
}
