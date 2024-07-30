package com.example.shop4All_backend.Controller;

import com.example.shop4All_backend.Entity.User;
import com.example.shop4All_backend.Service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
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

    // Handle POST requests to /registerNewUser to register a new user
    @PostMapping({"/registerNewSeller"})
    public ResponseEntity<?> registerNewSeller(@RequestBody User user) {
        try {
            User registeredUser = userService.registernewSeller(user);
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
    public String forAdmin(){
        return "This URL is only accessible to admin";
    }

    // Handle GET requests to /forSeller to provide content for sellers
    @GetMapping({"/forSeller"})
    @PreAuthorize("hasRole('seller')")
    public String forSeller(){
        return "This URL is only accessible to seller";
    }
}
