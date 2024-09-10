package com.example.shop4All_backend.controllers;

import com.example.shop4All_backend.entities.User;
import com.example.shop4All_backend.services.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PreRemove;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    // Initialize a demo admin
    @PostConstruct
    public void initRolesAndUsers() {
        userService.initRolesandUser();
    }

    // Handle POST requests to /registerNewSeller to register a new seller
    @PostMapping("/registerNewSeller")
    public ResponseEntity<User> registerNewSeller(@RequestBody User seller) {
        logger.info("Registering new seller");
        User registeredNewSeller = userService.registerNewSeller(seller);
        return new ResponseEntity<>(registeredNewSeller, HttpStatus.CREATED);
    }

    // Handle POST requests to /registerNewBuyer to register a new buyer
    @PostMapping("/registerNewBuyer")
    public ResponseEntity<User> registerNewBuyer(@RequestBody User buyer) {
        logger.info("Registering new buyer");
        User registeredUser = userService.registerBuyer(buyer);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    // Handle GET requests to /forAdmin to provide content for admins
    @GetMapping("admin/forAdmin")
    public ResponseEntity<String> forAdmin() {
        logger.info("For admin");
        return new ResponseEntity<>("This URL is only accessible to admin", HttpStatus.OK);
    }

    // Handle GET requests to /forSeller to provide content for sellers
    @GetMapping("seller/forSeller")
    public ResponseEntity<String> forSeller() {
        logger.info("For seller");
        return new ResponseEntity<>("This URL is only accessible to seller", HttpStatus.OK);
    }

    // Handle GET requests to /forBuyer to provide content for buyers
    @GetMapping("buyer/forBuyer")
    public ResponseEntity<String> forBuyer() {
        logger.info("For buyer");
        return new ResponseEntity<>("This URL is only accessible to buyer", HttpStatus.OK);
    }

    // Handle the request to activate an account of a seller
    @PostMapping("admin/activateAccount")
    public ResponseEntity<String> activateAccount(@RequestBody String email) {
        boolean activated = userService.activateAccount(email);
        if (activated) {
            logger.info("Account activated");
            return new ResponseEntity<>("Account activated", HttpStatus.OK);
        }
        logger.error("Account not activated");
        return new ResponseEntity<>("Account not activated", HttpStatus.BAD_REQUEST);
    }

    // Handle POST request to /declineAccount to decline a seller's account
    @PostMapping("admin/declineAccount")
    public ResponseEntity<String> declineAccount(@RequestBody String email) {
        return new ResponseEntity<>(userService.declineAccount(email), HttpStatus.OK);
    }

    // Handle GET request to /getNonActivatedAccounts to activate a seller's account
    @GetMapping("admin/getNonActivatedAccounts")
    public ResponseEntity<List<User>> getNonActivatedAccounts() {
        return new ResponseEntity<>(userService.getNonActivatedAccounts(), HttpStatus.OK);
    }

}
