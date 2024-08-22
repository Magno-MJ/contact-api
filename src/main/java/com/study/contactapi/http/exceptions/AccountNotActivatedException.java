package com.study.contactapi.http.exceptions;

import com.study.contactapi.http.exceptions.general.BadRequestException;

public class AccountNotActivatedException extends BadRequestException {

    public AccountNotActivatedException() {
        super("Account is not activated");
    }
}