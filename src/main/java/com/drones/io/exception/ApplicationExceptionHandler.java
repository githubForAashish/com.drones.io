package com.drones.io.exception;

import static com.drones.io.exception.ApplicationExceptionCodes.UNABLE_TO_PROCESS_REQUEST;

import com.drones.io.payload.ExceptionDTO;
import jakarta.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

  private final ExceptionMessageExtractor exceptionMessageExtractor;

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<ExceptionDTO> handleNoHandlerFoundException(
      NoHandlerFoundException exception) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
        .body(new ExceptionDTO(exception.getMessage()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ExceptionDTO> handleMethodArgumentNotValidException(
      MethodArgumentNotValidException exception) {
    List<String> errors =
        exception.getBindingResult().getFieldErrors().stream()
            .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
            .collect(Collectors.toList());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(new ExceptionDTO(String.join(",", errors)));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ExceptionDTO> handleHttpMessageNotReadableException(
      HttpMessageNotReadableException exception) {
    log.error("[VALIDATION-ERROR]: " + exception.getMessage());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(new ExceptionDTO(exception.getMessage()));
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ResponseEntity<ExceptionDTO> handleIllegalArgumentException(
      IllegalArgumentException exception) {
    log.error("[VALIDATION-ERROR]: " + exception.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(new ExceptionDTO(exception.getMessage()));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<ExceptionDTO> handleConstraintViolationException(
      ConstraintViolationException exception) {
    log.error("[VALIDATION-ERROR]: " + exception.getMessage());
    return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
        .body(new ExceptionDTO(exception.getLocalizedMessage()));
  }

  @ExceptionHandler(ApplicationException.class)
  public ResponseEntity<ExceptionDTO> handleApplicationExceptions(ApplicationException exception) {
    log.error("[APPLICATION-ERROR]: " + exception.getAppExcCode());
    return ResponseEntity.status(exception.getAppExcCode().getStatusCode())
        .body(
            new ExceptionDTO(
                exceptionMessageExtractor.getMessageFromCode(exception.getAppExcCode())));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ExceptionDTO> handleGenericErrors(Exception exception) {
    log.error("[INTERNAL-ERROR]: " + exception.getCause());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(
            new ExceptionDTO(
                exceptionMessageExtractor.getMessageFromCode(UNABLE_TO_PROCESS_REQUEST)));
  }
}
