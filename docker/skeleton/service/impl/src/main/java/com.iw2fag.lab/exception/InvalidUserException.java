package com.iw2fag.lab.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(value= HttpStatus.UNAUTHORIZED, reason="Invalid user")
public class InvalidUserException extends RuntimeException {
    public InvalidUserException() {

    }
}
