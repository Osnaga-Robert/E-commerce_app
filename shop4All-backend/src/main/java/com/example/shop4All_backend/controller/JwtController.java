package com.example.shop4All_backend.controller;

import com.example.shop4All_backend.dto.JwtRequest;
import com.example.shop4All_backend.dto.JwtResponse;
import com.example.shop4All_backend.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class JwtController {

    @Autowired
    private JwtService jwtService;

    // Handle POST requests to /authenticate to create a JWT token
    @PostMapping("/authenticate")
    public ResponseEntity<JwtResponse> createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return new ResponseEntity<>(jwtService.createJwtToken(jwtRequest), HttpStatus.CREATED);
    }
}
