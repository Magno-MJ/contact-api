package com.study.contactapi.http.exceptions.general;

import org.springframework.http.HttpStatus;

import com.study.contactapi.http.exceptions.general.customexception.CustomHttpException;

public class ForbiddenException extends CustomHttpException {

    public ForbiddenException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}