package com.example.shop4All_backend.controllers;

import com.example.shop4All_backend.entities.Image;
import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.exceptions.ProductException;
import com.example.shop4All_backend.exceptions.UserException;
import com.example.shop4All_backend.services.CategoryService;
import com.example.shop4All_backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Handle POST requests to /product/add to add a new product
    @PostMapping(value = {"/product/add"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Product> addNewProduct(@RequestPart(value = "product", required = false) Product product,
                                                 @RequestPart(value = "imageFile", required = false) MultipartFile[] file) {
        if (file == null || file[0].getContentType() == null || file[0].getContentType().isEmpty())
            throw new ProductException("File is empty");
        Set<Image> images = uploadImage(file);
        if (!categoryService.checkCategory(product))
            throw new ProductException("Category does not exist");
        product.setProductImages(images);
        return new ResponseEntity<>(productService.addNewProduct(product), HttpStatus.CREATED);
    }

    //Handle GET request to /product/getByCompanyName to get all products sold by a seller
    @GetMapping(value = {"/product/getByCompanyName"})
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<List<Product>> getAllProductsByCompany() {
        return new ResponseEntity<>(productService.getAllProductsByCompany(), HttpStatus.OK);
    }

    //Handle GET request to /product/getById/{productId} to get a product based on productId
    @GetMapping("/product/getById/{productId}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") Integer productId) {
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    //Handle DELETE request to /product/delete/{productId} to delete a product based on productId
    @DeleteMapping(value = {"/product/delete/{productId}"})
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Product> deleteProduct(@PathVariable("productId") Integer productId) {
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    //HANDLE POST request to /product/checkDiscount to check the fields of a discount
    @PostMapping(value = {"/product/checkDiscount"})
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Map<String, String>> checkDiscount(@RequestBody Product product) {
        productService.checkDiscount(product);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product added to list");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Handle POST request to /product/setDiscount to set a discount for a product
    @PostMapping(value = {"/product/setDiscount"})
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Product> setDiscount(@RequestBody Product product) {
        return new ResponseEntity<>(productService.setDiscount(product), HttpStatus.CREATED);
    }

    //Handle DELETE request to /product/deleteDiscount/{productId} to delete a discount from a product
    @DeleteMapping(value = {"/product/deleteDiscount/{productId}"})
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Product> deleteDiscount(@PathVariable("productId") Integer productId) {
        return new ResponseEntity<>(productService.deleteDiscount(productId), HttpStatus.ACCEPTED);
    }

    //format images to store in products object
    private Set<Image> uploadImage(MultipartFile[] multipartFile) {
        Set<Image> images = new HashSet<>();
        for (MultipartFile file : multipartFile) {
            Image image = null;
            try {
                image = new Image(file.getOriginalFilename(),
                        file.getContentType(),
                        file.getBytes()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            images.add(image);
        }
        return images;
    }
}
