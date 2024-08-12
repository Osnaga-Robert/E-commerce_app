package com.example.shop4All_backend.exceptions;

import com.example.shop4All_backend.entities.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionHandler {

    //handler for UserException
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {UserException.class})
    public ResponseEntity<Object> handleUserException(UserException e) {
        //create a custom exception
        CustomException customException = new CustomException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now(ZoneId.of("Europe/Bucharest"))
        );
        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }

    //handler for LogInException
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {LogInException.class})
    public ResponseEntity<Object> handleLoginException(LogInException e) {
        //create a custom exception
        CustomException customException = new CustomException(
                e.getMessage(),
                HttpStatus.UNAUTHORIZED,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(customException, HttpStatus.UNAUTHORIZED);
    }

    //handler for RegisterException
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {RegisterException.class})
    public ResponseEntity<Object> handleRegisterException(RegisterException e) {
        //create a custom exception
        CustomException customException = new CustomException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }

    //handler for ProductException
    @org.springframework.web.bind.annotation.ExceptionHandler(value = {ProductException.class})
    public ResponseEntity<Object> handleProductException(ProductException e) {
        //create a custom exception
        CustomException customException = new CustomException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }
}

