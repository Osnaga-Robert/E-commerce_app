package com.example.shop4All_backend.Controller;

import com.example.shop4All_backend.Entity.UserFactory.Buyer;
import com.example.shop4All_backend.Entity.UserFactory.Seller;
import com.example.shop4All_backend.Service.JwtService;
import com.example.shop4All_backend.Service.UserService;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
public class UserController {

    @Autowired
    private UserService userService;



    //initialize a demo admin
    @PostConstruct
    public void initRolesAndUsers(){
        userService.initRolesandUser();
    }

    // Handle POST requests to /registerNewSeller to register a new seller
    @PostMapping({"/registerNewSeller"})
    public ResponseEntity<?> registerNewSeller(@RequestBody Seller seller) {
        try {
            Seller registeredNewSeller = userService.registerNewSeller(seller);
            return new ResponseEntity<>(registeredNewSeller, HttpStatus.CREATED);
        } catch (ResponseStatusException ex) {
            return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred during registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Handle POST requests to /registerNewBuyer to register a new buyer
    @PostMapping({"registerNewBuyer"})
    public ResponseEntity<?> registerNewBuyer(@RequestBody Buyer buyer) {
        try {
            Buyer registeredUser = userService.registernewBuyer(buyer);
            return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
        } catch (ResponseStatusException ex) {
            return new ResponseEntity<>(ex.getReason(), ex.getStatusCode());
        } catch (Exception ex) {
            return new ResponseEntity<>("An error occurred during registration", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Handle GET requests to /forAdmin to provide content for admins
    @GetMapping({"/forAdmin"})
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> forAdmin(){
        return new ResponseEntity<>("This URL is only accessible to admin",HttpStatus.OK);
    }

    // Handle GET requests to /forSeller to provide content for sellers
    @GetMapping({"/forSeller"})
    @PreAuthorize("hasRole('seller')")
    public ResponseEntity<?> forSeller(){
        return new ResponseEntity<>("This URL is only accessible to seller",HttpStatus.OK);
    }

    // Handle GET requests to /forBuyer to provide content for buyers
    @GetMapping({"/forBuyer"})
    @PreAuthorize("hasRole('buyer')")
    public ResponseEntity<?> forBuyer(){
        return new ResponseEntity<>("This URL is only accessible to buyer",HttpStatus.OK);
    }

    //Hadle the request to activate an account of a seller
    @PostMapping({"/activateAccount"})
    @PreAuthorize("hasRole('admin')")
    public ResponseEntity<?> activateAccount(@RequestBody String email){
        boolean activated = userService.activateAccount(email);
        if(activated){
            return new ResponseEntity<>("Account activated", HttpStatus.OK);
        }
        return new ResponseEntity<>("Account not activated", HttpStatus.BAD_REQUEST);
    }
}
