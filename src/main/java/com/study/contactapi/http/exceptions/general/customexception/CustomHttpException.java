package com.study.contactapi.http.exceptions.general.customexception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomHttpException extends RuntimeException {
    private final String message;
    private final HttpStatus httpStatus;

    public CustomHttpException(String exceptionMessage, HttpStatus exceptionHttpStatus) {
        super(exceptionMessage);

        message = exceptionMessage;
        httpStatus = exceptionHttpStatus;
    }
}