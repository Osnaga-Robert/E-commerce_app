package com.example.shop4All_backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.time.ZonedDateTime;

@ControllerAdvice
public class ExceptionHandler{

    @org.springframework.web.bind.annotation.ExceptionHandler(value = {RequestException.class})
    public ResponseEntity<Object> handleException(RequestException e){
        CustomException customException = new CustomException(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(customException, HttpStatus.BAD_REQUEST);
    }
}
