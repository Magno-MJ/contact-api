package com.study.contactapi.http.exceptions;

import com.study.contactapi.http.exceptions.general.NotFoundException;

public class ConfirmationTokenNotFoundException extends NotFoundException {

    public ConfirmationTokenNotFoundException() {
        super("Confirmation token not found");
    }
}