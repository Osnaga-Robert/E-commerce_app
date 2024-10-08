package com.example.shop4All_backend.util;

import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ScheduledTask {

    private final ProductRepo productRepo;
    private final ProductService productService;

    //Scheduled task to run daily at 12 AM to check and remove expired discounts
    @Scheduled(cron = "0 0 0 * * *")
    @Async
    public void checkDiscounts() {
        LocalDate today = LocalDate.now();
        List<Product> products = (List<Product>) productRepo.findAll();
        for (Product product : products) {
            if (product.getProductDiscounted() != 0.0d && product.getProductToDiscounted().isBefore(today)) {
                productService.deleteProduct(product.getProductId());
            }
        }
    }
}
