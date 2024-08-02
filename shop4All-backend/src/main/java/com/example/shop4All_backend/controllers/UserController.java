package com.example.shop4All_backend.controllers;

import com.example.shop4All_backend.entities.User;
import com.example.shop4All_backend.services.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    // Initialize a demo admin
    @PostConstruct
    public void initRolesAndUsers() {
        userService.initRolesandUser();
    }

    // Handle POST requests to /registerNewSeller to register a new seller
    @PostMapping("/registerNewSeller")
    public ResponseEntity<User> registerNewSeller(@RequestBody User seller) {
        User registeredNewSeller = userService.registerNewSeller(seller);
        return new ResponseEntity<>(registeredNewSeller, HttpStatus.CREATED);
    }

    // Handle POST requests to /registerNewBuyer to register a new buyer
    @PostMapping("/registerNewBuyer")
    public ResponseEntity<User> registerNewBuyer(@RequestBody User buyer) {
        User registeredUser = userService.registernewBuyer(buyer);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    // Handle GET requests to /forAdmin to provide content for admins
    @GetMapping("/forAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> forAdmin() {
        return new ResponseEntity<>("This URL is only accessible to admin", HttpStatus.OK);
    }

    // Handle GET requests to /forSeller to provide content for sellers
    @GetMapping("/forSeller")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<String> forSeller() {
        return new ResponseEntity<>("This URL is only accessible to seller", HttpStatus.OK);
    }

    // Handle GET requests to /forBuyer to provide content for buyers
    @GetMapping("/forBuyer")
    @PreAuthorize("hasRole('BUYER')")
    public ResponseEntity<String> forBuyer() {
        return new ResponseEntity<>("This URL is only accessible to buyer", HttpStatus.OK);
    }

    // Handle the request to activate an account of a seller
    @PostMapping("/activateAccount")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> activateAccount(@RequestBody String email) {
        boolean activated = userService.activateAccount(email);
        if (activated) {
            return new ResponseEntity<>("Account activated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Account not activated", HttpStatus.BAD_REQUEST);
    }
}
