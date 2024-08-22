package com.study.contactapi.http.exceptions.general;

import org.springframework.http.HttpStatus;

import com.study.contactapi.http.exceptions.general.customexception.CustomHttpException;

public class BadRequestException extends CustomHttpException {

    public BadRequestException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}