package com.example.shop4All_backend.services;

import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.exceptions.ProductException;
import com.example.shop4All_backend.exceptions.UserException;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    @Autowired
    private UserRepo userRepo;

    //add a product
    public Product addNewProduct(Product product) {
        validateCredentials(product);
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        product.setCompanySeller(userRepo.findByUserEmail(userDetails.getUsername()).get().getUserCompanyName());
        return productRepo.save(product);
    }

    //get a product by productId
    public Product getProductById(Integer productId) {
        Optional<Product> optionalProduct = productRepo.findById(productId);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new ProductException("Product not found");
    }

    //returned products that are only sold by the logged seller
    public List<Product> getAllProductsByCompany() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companySeller = userRepo.findByUserEmail(userDetails.getUsername()).get().getUserCompanyName();
        List<Product> products = (List<Product>) productRepo.findAll();
        List<Product> filteredProducts = new ArrayList<>();
        for (Product product : products) {
            if (companySeller.equals(product.getCompanySeller())) {
                filteredProducts.add(product);
            }
        }
        return filteredProducts;
    }

    //delete a product from database
    public Product deleteProduct(Integer productId) {
        Product deletedProduct = productRepo.findById(productId).get();
        productRepo.deleteById(productId);
        return deletedProduct;
    }

    //add discount to a product
    public Product setDiscount(Product product) {
        checkDiscount(product);
        Product discountedProduct = checkProduct(product.getProductId());
        discountedProduct.setProductDiscounted(product.getProductDiscounted());
        discountedProduct.setProductFromDiscounted(product.getProductFromDiscounted());
        discountedProduct.setProductToDiscounted(product.getProductToDiscounted());
        return productRepo.save(discountedProduct);
    }

    //delete a discount from a product
    public Product deleteDiscount(Integer productId) {
        Product deletedProduct = checkProduct(productId);
        deletedProduct.setProductFromDiscounted(null);
        deletedProduct.setProductToDiscounted(null);
        deletedProduct.setProductDiscounted(0.0d);
        return productRepo.save(deletedProduct);
    }

    //check the discount fields
    public void checkDiscount(Product product) {
        LocalDate today = LocalDate.now();
        if (product.getProductFromDiscounted().isBefore(today)) {
            throw new ProductException("Your start date is before today.");
        } else if (product.getProductFromDiscounted().isEqual(product.getProductToDiscounted())) {
            throw new ProductException("Start date and end date are the same.");
        } else if (!product.getProductFromDiscounted().isBefore(product.getProductToDiscounted())) {
            throw new ProductException("Your start date is not before the end date.");
        }

        if (product.getProductDiscounted() <= 0 || product.getProductDiscounted() > 100) {
            throw new ProductException("Your discount is negative or above 100.");
        }
    }

    //check if the productId exists in the database
    public Product checkProduct(Integer productId) {
        Optional<Product> optionalProduct = productRepo.findById(productId);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        throw new ProductException("Product not found");
    }

    //check the product's fields
    private void validateCredentials(Product product) {
        if (product.getProductDescription() == null || product.getProductDescription().isEmpty() ||
                product.getProductName() == null || product.getProductName().isEmpty() ||
                product.getProductPrice() == 0 || product.getProductPrice() < 0 ||
                product.getProductQuantity() == 0 || product.getProductQuantity() < 0)
            throw new ProductException("Product fields cannot be empty or negative.");
    }
}
