package com.example.shop4All_backend.message_flow;

import com.example.shop4All_backend.configurations.JwtRequestFilter;
import com.example.shop4All_backend.entities.*;
import com.example.shop4All_backend.exceptions.ProductException;
import com.example.shop4All_backend.repositories.OrderDetailsRepo;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.repositories.ReviewRepo;
import com.example.shop4All_backend.repositories.UserRepo;
import com.example.shop4All_backend.services.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReviewTests {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private ReviewRepo reviewRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private OrderDetailsRepo orderDetailsRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        JwtRequestFilter.CURRENT_USER = "user@example.com";
    }

    @Test
    public void testAddReviewSuccess() {
        Integer productId = 1;
        Review review = new Review();
        review.setComment("Great product!");

        User user = new User();
        user.setUserEmail("user@example.com");
        user.setUserLastName("Doe");

        Product product = new Product();
        product.setProductId(productId);
        product.setProductReviews(new HashSet<>());

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setProduct(product);
        orderDetails.setOrderStatus("Delivered");

        when(userRepo.findByUserEmail("user@example.com")).thenReturn(Optional.of(user));
        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        when(orderDetailsRepo.findByUser(user)).thenReturn(Collections.singletonList(orderDetails));
        when(reviewRepo.save(any(Review.class))).thenReturn(review);
        when(productRepo.save(any(Product.class))).thenReturn(product);

        Product updatedProduct = reviewService.addReview(productId, review);

        assertNotNull(updatedProduct, "Product should not be null");
        assertTrue(updatedProduct.getProductReviews().contains(review), "Product reviews should contain the new review");
        assertEquals("user@example.com", review.getBuyerEmail(), "Review buyer email should match current user email");
        assertEquals("Doe", review.getBuyerName(), "Review buyer name should match current user last name");
    }

    @Test
    public void testAddReviewUserAlreadyReviewed() {
        Integer productId = 1;
        Review review = new Review();
        review.setComment("Great product!");
        review.setRating(3);

        User user = new User();
        user.setUserEmail("user@example.com");
        user.setUserLastName("Doe");

        Product product = new Product();
        product.setProductId(productId);
        Review existingReview = new Review();
        existingReview.setBuyerEmail("user@example.com");
        product.setProductReviews(new HashSet<>(Collections.singletonList(existingReview)));

        when(userRepo.findByUserEmail("user@example.com")).thenReturn(Optional.of(user));
        when(productRepo.findById(productId)).thenReturn(Optional.of(product));

        ProductException thrown = assertThrows(ProductException.class, () -> {
            reviewService.addReview(productId, review);
        });

        assertEquals("User with this email already reviewed the product", thrown.getMessage());
    }

    @Test
    public void testAddReviewNoOrderFound() {
        Integer productId = 1;
        Review review = new Review();
        review.setComment("Great product!");
        review.setRating(3);

        User user = new User();
        user.setUserEmail("user@example.com");
        user.setUserLastName("Doe");

        Product product = new Product();
        product.setProductId(productId);
        product.setProductReviews(new HashSet<>());

        when(userRepo.findByUserEmail("user@example.com")).thenReturn(Optional.of(user));
        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        when(orderDetailsRepo.findByUser(user)).thenReturn(Collections.emptyList());

        ProductException thrown = assertThrows(ProductException.class, () -> {
            reviewService.addReview(productId, review);
        });

        assertEquals("Error", thrown.getMessage());
    }

    @Test
    public void testAddReviewProductNotOrdered() {
        Integer productId = 1;
        Review review = new Review();
        review.setComment("Great product!");
        review.setRating(3);

        User user = new User();
        user.setUserEmail("user@example.com");
        user.setUserLastName("Doe");

        Product product = new Product();
        product.setProductId(productId);
        product.setProductReviews(new HashSet<>());

        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setProduct(new Product());
        orderDetails.setOrderStatus("Delivered");

        when(userRepo.findByUserEmail("user@example.com")).thenReturn(Optional.of(user));
        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        when(orderDetailsRepo.findByUser(user)).thenReturn(Collections.singletonList(orderDetails));

        ProductException thrown = assertThrows(ProductException.class, () -> {
            reviewService.addReview(productId, review);
        });

        assertEquals("Review not added", thrown.getMessage());
    }
}
