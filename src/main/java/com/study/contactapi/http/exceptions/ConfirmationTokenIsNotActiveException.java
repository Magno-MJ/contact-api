package com.study.contactapi.http.exceptions;

import com.study.contactapi.http.exceptions.general.BadRequestException;

public class ConfirmationTokenIsNotActiveException extends BadRequestException {

    public ConfirmationTokenIsNotActiveException() {
        super("Confirmation token is not active");
    }
}