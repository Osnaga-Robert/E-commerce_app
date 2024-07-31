package com.example.shop4All_backend.Controller;

import com.example.shop4All_backend.DTO.JwtRequest;
import com.example.shop4All_backend.DTO.JwtResponse;
import com.example.shop4All_backend.Service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
    public ResponseEntity<?> createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return new ResponseEntity<>(jwtService.createJwtToken(jwtRequest), HttpStatus.CREATED);
    }
}
