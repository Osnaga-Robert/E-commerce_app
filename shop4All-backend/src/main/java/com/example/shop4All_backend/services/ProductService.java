package com.example.shop4All_backend.services;

import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    //add a product
    public Product addNewProduct(Product product) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        product.setCompanySeller(userRepo.findByUserEmail(userDetails.getUsername()).get().getUserCompanyName());
        return productRepo.save(product);
    }
}
