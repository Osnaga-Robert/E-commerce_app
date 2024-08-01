package com.example.shop4All_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class RegisterException extends ResponseStatusException {
    public RegisterException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
