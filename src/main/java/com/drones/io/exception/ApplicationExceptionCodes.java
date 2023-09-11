package com.drones.io.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ApplicationExceptionCodes {
  DRONE_NOT_FOUND(HttpStatus.NOT_FOUND),
  FLEET_CAPACITY_REACHED(HttpStatus.INSUFFICIENT_STORAGE),
  DRONE_OVERLOADED(HttpStatus.PAYLOAD_TOO_LARGE),
  DRONE_PREOCCUPIED(HttpStatus.SERVICE_UNAVAILABLE),
  DRONE_BATTERY_TOO_LOW(HttpStatus.SERVICE_UNAVAILABLE),
  DRONE_ALREADY_REGISTERED(HttpStatus.CONFLICT),
  UNABLE_TO_PROCESS_REQUEST(HttpStatus.INTERNAL_SERVER_ERROR);

  private final HttpStatus statusCode;

  @Override
  public String toString() {
    return this.name();
  }
}
