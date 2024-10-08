package com.example.shop4All_backend.controllers;

import com.example.shop4All_backend.dtos.JwtRequest;
import com.example.shop4All_backend.dtos.JwtResponse;
import com.example.shop4All_backend.services.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class JwtController {

    private final JwtService jwtService;

    // Handle POST requests to /authenticate to create a JWT token
    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> createJwtToken(@RequestBody JwtRequest jwtRequest) {
        return new ResponseEntity<>(jwtService.createJwtToken(jwtRequest), HttpStatus.CREATED);
    }
}
