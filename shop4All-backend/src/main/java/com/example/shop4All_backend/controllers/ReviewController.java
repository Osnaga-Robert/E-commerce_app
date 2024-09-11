package com.example.shop4All_backend.controllers;

import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.entities.Review;
import com.example.shop4All_backend.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
public class ReviewController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ReviewService reviewService;

    // Handle POST request to /review/add/{productId} to add a review
    @PostMapping({"buyer/review/add/{productId}"})
    public ResponseEntity<Product> addReview(@PathVariable("productId") Integer productId, @RequestBody Review review) {
        logger.info("Adding a new review");
        return new ResponseEntity<>(reviewService.addReview(productId, review), HttpStatus.CREATED);
    }

    //Handle GET request to /review/get/{productId} to see all reviews of a product
    @GetMapping({"review/get/{productId}"})
    public ResponseEntity<List<Review>> getReview(@PathVariable("productId") Integer productId) {
        logger.info("Getting reviews");
        return new ResponseEntity<>(reviewService.getReviews(productId), HttpStatus.OK);
    }
}
