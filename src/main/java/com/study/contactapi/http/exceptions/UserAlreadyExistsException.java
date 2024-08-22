package com.study.contactapi.http.exceptions;

import com.study.contactapi.http.exceptions.general.BadRequestException;

public class UserAlreadyExistsException extends BadRequestException {

    public UserAlreadyExistsException() {
        super("User already exists");
    }
}