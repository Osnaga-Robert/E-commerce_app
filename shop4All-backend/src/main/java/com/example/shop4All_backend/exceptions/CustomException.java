package com.example.shop4All_backend.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
public class CustomException {
    private final String message;
    private final HttpStatus status;
    private final ZonedDateTime timestamp;

    public CustomException(String message, HttpStatus status, ZonedDateTime timestamp) {
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

}
