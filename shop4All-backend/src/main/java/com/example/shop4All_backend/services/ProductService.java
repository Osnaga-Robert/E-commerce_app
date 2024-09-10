package com.example.shop4All_backend.services;

import com.example.shop4All_backend.configurations.JwtRequestFilter;
import com.example.shop4All_backend.controllers.CartController;
import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.entities.User;
import com.example.shop4All_backend.entities.Cart;
import com.example.shop4All_backend.exceptions.ProductException;
import com.example.shop4All_backend.exceptions.UserException;
import com.example.shop4All_backend.repositories.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
    private final ProductRepo productRepo;
    private final ReviewRepo reviewRepo;
    private final UserRepo userRepo;
    private final CartRepo cartRepo;
    private final OrderDetailsRepo orderDetailsRepo;

    //add a product
    public Product addNewProduct(Product product) {
        if (!validateCredentials(product)) {
            logger.error("Invalid credentials");
            throw new ProductException("Invalid credentials");
        }
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        product.setCompanySeller(userRepo.findByUserEmail(userDetails.getUsername()).get().getUserCompanyName());
        product.setActive(true);
        logger.info("Adding new product");
        return productRepo.save(product);
    }

    //get a product by productId
    public Product getProductById(Integer productId) {
        Optional<Product> optionalProduct = productRepo.findById(productId);
        if (optionalProduct.isPresent()) {
            logger.info("Found product with id: " + productId);
            return optionalProduct.get();
        }
        logger.error("No product found with id: " + productId);
        throw new ProductException("Product not found");
    }

    //get all products using pagination
    public List<Product> getAllProducts(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, 10);
        Page<Product> productPage = productRepo.findByIsActiveTrue(pageable);
        logger.info("Getting all products from the page " + pageNumber);
        return productPage.getContent();
    }

    //returned products that are only sold by the logged seller
    public List<Product> getAllProductsByCompany(int pageNumber) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String companySeller = userRepo.findByUserEmail(userDetails.getUsername()).get().getUserCompanyName();
        Pageable pageable = PageRequest.of(pageNumber, 10);
        Page<Product> productPage = (Page<Product>) productRepo.findByCompanySeller(companySeller, pageable);
        logger.info("Getting all products from the page " + pageNumber);
        return productPage.getContent();
    }

    //get all products based on company
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
        logger.info("Got all products based on company seller " + companySeller);
        return filteredProducts;
    }

    //delete a product from database
    @Transactional
    public Product deleteProduct(Integer productId) {
        Product deletedProduct = productRepo.findById(productId).get();
        cartRepo.deleteByProduct(deletedProduct);
        orderDetailsRepo.deleteByProduct(deletedProduct);
        productRepo.deleteById(productId);
        logger.info("Deleted product with id: " + productId);
        return deletedProduct;
    }

    //add discount to a product
    public Product setDiscount(Product product) {
        checkDiscount(product);
        Product discountedProduct = checkProduct(product.getProductId());
        discountedProduct.setProductDiscounted(product.getProductDiscounted());
        discountedProduct.setProductFromDiscounted(product.getProductFromDiscounted());
        discountedProduct.setProductToDiscounted(product.getProductToDiscounted());
        logger.info("Discounted product with id: " + discountedProduct.getProductId());
        return productRepo.save(discountedProduct);
    }

    //delete a discount from a product
    public Product deleteDiscount(Integer productId) {
        Product deletedProduct = checkProduct(productId);
        deletedProduct.setProductFromDiscounted(null);
        deletedProduct.setProductToDiscounted(null);
        deletedProduct.setProductDiscounted(0.0d);
        logger.info("Delete product discount with id: " + productId);
        return productRepo.save(deletedProduct);
    }

    //check the discount fields
    public void checkDiscount(Product product) {
        LocalDate today = LocalDate.now();
        if (product.getProductFromDiscounted().isBefore(today)) {
            logger.error("Product from discounted is before today");
            throw new ProductException("Your start date (" + product.getProductFromDiscounted() + ") is before today (" + today + ") .");
        } else if (product.getProductFromDiscounted().isEqual(product.getProductToDiscounted())) {
            logger.error("Product from discounted is equal to today");
            throw new ProductException("Start date (" + product.getProductFromDiscounted() + ") and end date (" + product.getProductToDiscounted() + ") are the same.");
        } else if (!product.getProductFromDiscounted().isBefore(product.getProductToDiscounted())) {
            logger.error("Your start date is not before the end date");
            throw new ProductException("Your start (" + product.getProductFromDiscounted() + ") date is not before the end date (" + product.getProductToDiscounted() + ").");
        }

        if (product.getProductDiscounted() <= 0 || product.getProductDiscounted() > 100) {
            logger.error("Discount is negative or above 100");
            throw new ProductException("Your discount is negative or above 100.");
        }
    }

    //check if the productId exists in the database
    public Product checkProduct(Integer productId) {
        Optional<Product> optionalProduct = productRepo.findById(productId);
        if (optionalProduct.isPresent()) {
            return optionalProduct.get();
        }
        logger.error("No product found with id: " + productId);
        throw new ProductException("Product not found");
    }

    //get all products from a cart or get a product that buyer want to buy directly
    public List<Product> getProductDetails(boolean isSingleProductCheckout, Integer productId) {
        if (isSingleProductCheckout && productId != 0) {
            List<Product> products = new ArrayList<>();
            Product product = productRepo.findById(productId).get();
            products.add(product);
            return products;
        } else {
            String buyerEmail = JwtRequestFilter.CURRENT_USER;
            User buyer = userRepo.findByUserEmail(buyerEmail).get();
            List<Cart> carts = cartRepo.findByUser(buyer);
            List<Product> products = new ArrayList<>();
            for (Cart cart : carts)
                products.add((Product) cart.getProduct().toArray()[0]);
            return products;
        }
    }

    public Product statusProduct(Integer productId) {
        Product product = productRepo.findById(productId).get();
        if (product.getProductQuantity() <= 0) {
            logger.error("No stock");
            throw new ProductException("No stock");
        }
        if (product.isActive())
            product.setActive(false);
        else
            product.setActive(true);
        return productRepo.save(product);
    }

    //check the product's fields
    private boolean validateCredentials(Product product) {
        if (product.getProductDescription() == null || product.getProductDescription().isEmpty() ||
                product.getProductName() == null || product.getProductName().isEmpty() ||
                product.getProductPrice() == 0 || product.getProductPrice() < 0 ||
                product.getProductQuantity() == 0 || product.getProductQuantity() < 0)
            return false;
        return true;
    }
}
