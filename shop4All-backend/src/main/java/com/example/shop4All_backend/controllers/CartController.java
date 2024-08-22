package com.example.shop4All_backend.controllers;

import com.example.shop4All_backend.entities.Cart;
import com.example.shop4All_backend.services.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    //Handle GET request to /cart/addProduct/{productId} to add a product to buyer's cart
    @PreAuthorize("hasRole('BUYER')")
    @GetMapping({"/cart/addProduct/{productId}"})
    public ResponseEntity<Cart> addToCart(@PathVariable(name = "productId") Integer productId) {
        return new ResponseEntity<>(cartService.addToCart(productId), HttpStatus.CREATED);
    }

    //Handle GET request to /cart/getDetails to get all products from buyer's cart
    @PreAuthorize("hasRole('BUYER')")
    @GetMapping({"/cart/getDetails"})
    public ResponseEntity<List<Cart>> getCartDetails() {
        return new ResponseEntity<>(cartService.getCatDetails(), HttpStatus.OK);
    }

    //Handle GET request to /cart/deleteItem/{cartId} to delete a product from buyer's cart
    @PreAuthorize("hasRole('BUYER')")
    @DeleteMapping({"/cart/deleteItem/{cartId}"})
    public ResponseEntity<Cart> deleteCart(@PathVariable(name = "cartId") Integer cartId) {
        return new ResponseEntity<>(cartService.deleteCartItem(cartId), HttpStatus.CREATED);
    }
}
