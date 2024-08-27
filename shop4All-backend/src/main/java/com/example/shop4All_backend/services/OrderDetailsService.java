package com.example.shop4All_backend.services;

import com.example.shop4All_backend.configurations.JwtRequestFilter;
import com.example.shop4All_backend.controllers.CartController;
import com.example.shop4All_backend.entities.*;
import com.example.shop4All_backend.repositories.CartRepo;
import com.example.shop4All_backend.repositories.OrderDetailsRepo;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailsService {

    private final OrderDetailsRepo orderDetailsRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final CartRepo cartRepo;
    private static final String ORDER_PLACED = "Placed";
    private static final Logger logger = LoggerFactory.getLogger(OrderDetailsService.class);


    //place an order
    public OrderDetails placeOrder(boolean isSingleProductCheckout, OrderInput orderInput) {
        List<OrderProductQuantity> productQuantityList = orderInput.getProducts();
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepo.findByUserEmail(currentUser).get();
        OrderDetails orderDetails = new OrderDetails();

        for (OrderProductQuantity orderProductQuantity : productQuantityList) {
            Product product = productRepo.findById(orderProductQuantity.getProductId()).get();

            orderDetails = OrderDetails.builder()
                    .orderFullName(orderInput.getFullName())
                    .orderFullAddress(orderInput.getFullAddress())
                    .orderContactNumber(orderInput.getContactNumber())
                    .orderStatus(ORDER_PLACED)
                    .orderAmount(product.getProductPrice() * (1 - product.getProductDiscounted() / 100) * orderProductQuantity.getQuantity())
                    .product(product)
                    .user(user)
                    .build();
        }

        if (!isSingleProductCheckout) {
            List<Cart> cart = cartRepo.findByUser(user);
            cartRepo.deleteAll(cart);
        }

        logger.info("Order placed");
        return orderDetailsRepo.save(orderDetails);
    }
}
