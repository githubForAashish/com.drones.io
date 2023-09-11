package com.drones.io.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApplicationException extends RuntimeException {
  private final ApplicationExceptionCodes appExcCode;

  public static ApplicationException create(ApplicationExceptionCodes appExcCode) {
    return new ApplicationException(appExcCode);
  }
}
