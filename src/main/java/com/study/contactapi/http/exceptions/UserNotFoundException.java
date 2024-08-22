package com.study.contactapi.http.exceptions;

import com.study.contactapi.http.exceptions.general.NotFoundException;

public class UserNotFoundException extends NotFoundException {

    public UserNotFoundException() {
        super("User not found");
    }
}
