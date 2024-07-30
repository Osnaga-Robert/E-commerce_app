package com.example.shop4All_backend.Controller;

import com.example.shop4All_backend.Entity.JwtRequest;
import com.example.shop4All_backend.Entity.JwtResponse;
import com.example.shop4All_backend.Service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public JwtResponse createJwtToken(@RequestBody JwtRequest jwtRequest) throws Exception {
        return jwtService.createJwtToken(jwtRequest);
    }


}
