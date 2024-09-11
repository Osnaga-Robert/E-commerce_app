package com.example.shop4All_backend.controllers;

import com.example.shop4All_backend.entities.OrderDetails;
import com.example.shop4All_backend.entities.OrderInput;
import com.example.shop4All_backend.services.OrderDetailsService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderDetailsController {

    private static final Logger logger = LoggerFactory.getLogger(OrderDetailsController.class);
    @Autowired
    private final OrderDetailsService orderDetailsService;

    // Handle POST request to /placeOrder/{isSingleProductCheckout} to place the buyer's order
    @PostMapping("buyer/placeOrder/{isSingleProductCheckout}")
    public ResponseEntity<List<OrderDetails>> placeOrder(@PathVariable(name = "isSingleProductCheckout") boolean isSingleProductCheckout, @RequestBody OrderInput orderInput) {
        logger.info("Order placed");
        return new ResponseEntity<>(orderDetailsService.placeOrder(isSingleProductCheckout, orderInput), HttpStatus.CREATED);
    }

    // Handle GET request to /order/getAll to get all orders made
    @GetMapping("buyer/order/getAll")
    public ResponseEntity<List<OrderDetails>> getAllOrders() {
        logger.info("Get all orders");
        return new ResponseEntity<>(orderDetailsService.getAllOrders(), HttpStatus.OK);
    }

    // Handle GET request to /order/getAllSeller to get all orders made by a seller
    @GetMapping("seller/order/getAllSeller")
    public ResponseEntity<List<OrderDetails>> getAllOrdersSeller() {
        logger.info("Get all orders for seller");
        return new ResponseEntity<>(orderDetailsService.getAllOrdersSeller(), HttpStatus.OK);
    }

    // Handle GET request to /order/delivered/{orderId} to mark an order as delivered
    @GetMapping("seller/order/delivered/{orderId}")
    public ResponseEntity<OrderDetails> markOrderDelivered(@PathVariable(name = "orderId") Integer orderId) {
        logger.info("Mark order delivered");
        return new ResponseEntity<>(orderDetailsService.markOrderDelivered(orderId), HttpStatus.OK);
    }
}
