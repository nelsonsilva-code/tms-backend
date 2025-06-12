package com.nelson.tms.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

public class TodoNotFoundException extends RuntimeException {

    public TodoNotFoundException(){
        super();
    }
}
