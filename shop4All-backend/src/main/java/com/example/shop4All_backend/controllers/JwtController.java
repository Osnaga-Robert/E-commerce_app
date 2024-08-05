package com.example.shop4All_backend.controllers;

import com.example.shop4All_backend.dtos.JwtRequest;
import com.example.shop4All_backend.dtos.JwtResponse;
import com.example.shop4All_backend.services.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.security.auth.login.LoginException;

@RestController
@CrossOrigin
public class JwtController {

    @Autowired
    private JwtService jwtService;

    // Handle POST requests to /authenticate to create a JWT token
    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> createJwtToken(@RequestBody JwtRequest jwtRequest) {
        return new ResponseEntity<>(jwtService.createJwtToken(jwtRequest), HttpStatus.CREATED);
    }
}
