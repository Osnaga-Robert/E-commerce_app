package com.example.shop4All_backend.controllers;

import com.example.shop4All_backend.entities.OrderDetails;
import com.example.shop4All_backend.entities.OrderInput;
import com.example.shop4All_backend.services.OrderDetailsService;
import com.example.shop4All_backend.services.ProductService;
import groovy.transform.AutoImplement;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderDetailsController {

    @Autowired
    private final OrderDetailsService orderDetailsService;

    // Handle POST request to /placeOrder/{isSingleProductCheckout} to place the buyer's order
    @PreAuthorize("hasRole('BUYER')")
    @PostMapping("/placeOrder/{isSingleProductCheckout}")
    public ResponseEntity<OrderDetails> placeOrder(@PathVariable(name = "isSingleProductCheckout") boolean isSingleProductCheckout, @RequestBody OrderInput orderInput) {
        return new ResponseEntity<>(orderDetailsService.placeOrder(isSingleProductCheckout, orderInput), HttpStatus.CREATED);
    }
}
