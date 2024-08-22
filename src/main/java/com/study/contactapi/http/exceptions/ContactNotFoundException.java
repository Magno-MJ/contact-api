package com.study.contactapi.http.exceptions;

import com.study.contactapi.http.exceptions.general.NotFoundException;

public class ContactNotFoundException extends NotFoundException {

    public ContactNotFoundException() {
        super("Contact not found");
    }
}
