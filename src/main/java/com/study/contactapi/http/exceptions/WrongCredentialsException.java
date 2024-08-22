package com.study.contactapi.http.exceptions;

import com.study.contactapi.http.exceptions.general.BadRequestException;

public class WrongCredentialsException extends BadRequestException {

    public WrongCredentialsException() {
        super("The credentials are wrong");
    }
}