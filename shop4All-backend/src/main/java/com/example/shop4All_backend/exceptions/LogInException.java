package com.example.shop4All_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class LogInException extends ResponseStatusException {
    public LogInException(String message) {
        super(HttpStatus.CONFLICT, message);
    }
}
