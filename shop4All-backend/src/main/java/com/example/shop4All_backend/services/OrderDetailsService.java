package com.example.shop4All_backend.services;

import com.example.shop4All_backend.configurations.JwtRequestFilter;
import com.example.shop4All_backend.controllers.CartController;
import com.example.shop4All_backend.entities.*;
import com.example.shop4All_backend.exceptions.ProductException;
import com.example.shop4All_backend.exceptions.UserException;
import com.example.shop4All_backend.repositories.CartRepo;
import com.example.shop4All_backend.repositories.OrderDetailsRepo;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(OrderDetailsService.class);
    private final OrderDetailsRepo orderDetailsRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;
    private final CartRepo cartRepo;
    private final EmailService emailService;
    private static final String ORDER_PLACED = "Placed";


    //place an order
    public List<OrderDetails> placeOrder(boolean isSingleProductCheckout, OrderInput orderInput) {
        checkOrderInput(orderInput);
        List<OrderProductQuantity> productQuantityList = orderInput.getProducts();
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepo.findByUserEmail(currentUser).get();
        List<OrderDetails> orderDetailsList = new ArrayList<>();

        for (OrderProductQuantity orderProductQuantity : productQuantityList) {
            Product product = productRepo.findById(orderProductQuantity.getProductId()).get();
            if (product.getProductQuantity() < orderProductQuantity.getQuantity()) {
                throw new ProductException("Not enough stock for product " + product.getProductName());
            }
        }

        for (OrderProductQuantity orderProductQuantity : productQuantityList) {
            Product product = productRepo.findById(orderProductQuantity.getProductId()).get();

            OrderDetails orderDetails = OrderDetails.builder()
                    .orderFullName(orderInput.getFullName())
                    .orderFullAddress(orderInput.getFullAddress())
                    .orderContactNumber(orderInput.getContactNumber())
                    .orderStatus(ORDER_PLACED)
                    .orderAmount(product.getProductPrice() * (1 - product.getProductDiscounted() / 100) * orderProductQuantity.getQuantity())
                    .product(product)
                    .user(user)
                    .build();
            product.setProductQuantity(product.getProductQuantity() - orderProductQuantity.getQuantity());
            if (product.getProductQuantity() <= 5 && product.getProductQuantity() > 0) {
                emailService.sendEmail(userRepo.findByUserCompanyName(product.getCompanySeller()).get().getUserEmail(), "Shop4All", "Low stock " + product.getProductName() + " with id: " + product.getProductId());
            }
            if (product.getProductQuantity() <= 0) {
                emailService.sendEmail(userRepo.findByUserCompanyName(product.getCompanySeller()).get().getUserEmail(), "Shop4All", "No stock for " + product.getProductName() + " with id: " + product.getProductId());
                product.setActive(false);
            }
            productRepo.save(product);
            orderDetailsList.add(orderDetails);
        }

        if (!isSingleProductCheckout) {
            List<Cart> cart = cartRepo.findByUser(user);
        }

        logger.info("Order placed");
        emailService.sendEmail(user.getUserEmail(), "Shop4All", "Your order has been sent to us");
        orderDetailsRepo.saveAll(orderDetailsList);
        return orderDetailsList;
    }

    //check the parameters of an OrderInput
    private void checkOrderInput(OrderInput orderInput) {
        if (orderInput.getContactNumber() == null || orderInput.getContactNumber().isEmpty() ||
                orderInput.getFullAddress() == null || orderInput.getFullAddress().isEmpty() ||
                orderInput.getFullName() == null || orderInput.getFullName().isEmpty())
            throw new UserException("Fill all the required fields");
    }

    //get all orders made by a buyer
    public List<OrderDetails> getAllOrders() {
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepo.findByUserEmail(currentUser).get();
        logger.info("Get all orders");
        return orderDetailsRepo.findByUser(user);
    }

    //get all orders made by a seller
    public List<OrderDetails> getAllOrdersSeller() {
        String currentUser = JwtRequestFilter.CURRENT_USER;
        User user = userRepo.findByUserEmail(currentUser).get();

        List<OrderDetails> allOrdersSeller = new ArrayList<>();
        String userCompanyName = user.getUserCompanyName(); // Get the current user's company name

        orderDetailsRepo.findAll().forEach(orderDetails -> {
            if (orderDetails.getProduct().getCompanySeller().equals(userCompanyName)) {
                allOrdersSeller.add(orderDetails);
            }
        });

        logger.info("Get all orders make by a seller");
        return allOrdersSeller;
    }

    //mark delivered orders
    public OrderDetails markOrderDelivered(Integer orderId) {
        OrderDetails orderDetails = orderDetailsRepo.findById(orderId).get();
        if (orderDetails != null) {
            orderDetails.setOrderStatus("Delivered");
            logger.info("Order delivered");
            return orderDetailsRepo.save(orderDetails);
        }
        logger.error("Order does not exist");
        emailService.sendEmail(orderDetails.getUser().getUserEmail(), "Shop4All", "Your order has been delivered");
        throw new UserException("Order does not exist");
    }
}
