package com.example.shop4All_backend.services;

import com.example.shop4All_backend.configurations.JwtRequestFilter;
import com.example.shop4All_backend.entities.OrderDetails;
import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.entities.Review;
import com.example.shop4All_backend.entities.User;
import com.example.shop4All_backend.exceptions.ProductException;
import com.example.shop4All_backend.repositories.OrderDetailsRepo;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.repositories.ReviewRepo;
import com.example.shop4All_backend.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    private final ProductRepo productRepo;
    private final ReviewRepo reviewRepo;
    private final UserRepo userRepo;
    private final OrderDetailsRepo orderDetailsRepo;

    //add a review to a product
    public Product addReview(Integer productId, Review review) {
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepo.findByUserEmail(currentUser).get();
        Product product = productRepo.findById(productId).get();
        boolean checkReview = product.getProductReviews().stream().anyMatch(products -> products.getBuyerEmail().equals(currentUser));
        if (checkReview) {
            logger.error("User with this email already reviewed the product");
            throw new ProductException("User with this email already reviewed the product");
        }
        List<OrderDetails> userOrders = orderDetailsRepo.findByUser(user);
        if (userOrders.isEmpty()) {
            logger.error("Error");
            throw new ProductException("Error");
        }
        boolean orderedProduct = userOrders.stream().anyMatch(order -> order.getProduct().equals(product) && order.getOrderStatus().equals("Delivered"));
        if (orderedProduct) {
            review.setBuyerEmail(user.getUserEmail());
            review.setBuyerName(user.getUserLastName());
            reviewRepo.save(review);
            Set<Review> reviewSet = product.getProductReviews();
            reviewSet.add(review);
            product.setProductReviews(reviewSet);
            logger.info("Reviewed added");
            return productRepo.save(product);
        }
        logger.error("Error");
        throw new ProductException("Review not added");
    }

    //get the reviews of a product
    public List<Review> getReviews(Integer productId) {
        Product product = productRepo.findById(productId).get();
        logger.info("Get all reviews of a product");
        return new ArrayList<>(product.getProductReviews());
    }
}
