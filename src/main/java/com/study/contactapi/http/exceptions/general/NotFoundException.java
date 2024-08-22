package com.study.contactapi.http.exceptions.general;

import org.springframework.http.HttpStatus;

import com.study.contactapi.http.exceptions.general.customexception.CustomHttpException;

public class NotFoundException extends CustomHttpException {

    public NotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
