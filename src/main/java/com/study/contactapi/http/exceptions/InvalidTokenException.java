package com.study.contactapi.http.exceptions;

import com.study.contactapi.http.exceptions.general.ForbiddenException;

public class InvalidTokenException extends ForbiddenException {
    public InvalidTokenException() {
        super("Invalid token");
    }
}