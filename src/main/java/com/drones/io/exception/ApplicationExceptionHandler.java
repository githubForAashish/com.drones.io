package com.drones.io.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RequiredArgsConstructor
@RestControllerAdvice
public class ApplicationExceptionHandler {

    private final ExceptionMessageExtractor exceptionMessageExtractor;

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<String> handleApplicationExceptions(ApplicationException exception) {
        return ResponseEntity.status(exception.getAppExcCode().getStatusCode()).body(exceptionMessageExtractor.getMessageFromCode(exception.getAppExcCode()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<String> handleValidationExceptions(RuntimeException exception) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(exception.getMessage());
    }

}
