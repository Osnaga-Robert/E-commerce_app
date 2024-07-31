package com.example.shop4All_backend.Controller;

import com.example.shop4All_backend.Entity.Role;
import com.example.shop4All_backend.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RoleController {

    @Autowired
    private RoleService roleService;

    // Handle POST requests to /createNewRole to create a new role
    @PostMapping({"/createNewRole"})
    public ResponseEntity<?> createNewRole(@RequestBody Role role) {
        return new ResponseEntity<>(roleService.createNewRole(role), HttpStatus.OK);
    }


}
