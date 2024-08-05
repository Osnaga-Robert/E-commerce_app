package com.example.shop4All_backend.exceptions;

import javax.security.auth.login.LoginException;

public class LogInException extends RuntimeException {
    public LogInException(String message) {
        super(message);
    }

    public LogInException(String message, Throwable cause) {
        super(message, cause);
    }
}
