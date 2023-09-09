package com.drones.io.exception;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ExceptionMessageExtractor {
    private final MessageSource messageSource;

    public String getMessageFromCode(ApplicationExceptionCodes appErrorCode) {
        return messageSource.getMessage(appErrorCode.toString(), null, Locale.getDefault());
    }
}