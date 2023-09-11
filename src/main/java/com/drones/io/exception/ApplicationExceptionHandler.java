package com.drones.io.exception;

import com.drones.io.payload.ExceptionDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

import static com.drones.io.exception.ApplicationExceptionCodes.UNABLE_TO_PROCESS_REQUEST;

@RequiredArgsConstructor
@RestControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

    private final ExceptionMessageExtractor exceptionMessageExtractor;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDTO> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors()
                .stream().map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage()).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ExceptionDTO(String.join(",", errors)));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDTO> handleValidationExceptions(RuntimeException exception) {
        log.error("[VALIDATION-ERROR]: " + exception.getCause());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(new ExceptionDTO(exception.getMessage()));
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ExceptionDTO> handleApplicationExceptions(ApplicationException exception) {
        log.error("[APPLICATION-ERROR]: " + exception.getAppExcCode());
        return ResponseEntity.status(exception.getAppExcCode().getStatusCode()).body(new ExceptionDTO(exceptionMessageExtractor.getMessageFromCode(exception.getAppExcCode())));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionDTO> handleGenericErrors(Exception exception) {
        log.error("[INTERNAL-ERROR]: " + exception.getCause());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionDTO(exceptionMessageExtractor.getMessageFromCode(UNABLE_TO_PROCESS_REQUEST)));
    }

}
