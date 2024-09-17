package com.example.shop4All_backend.controllers;

import com.example.shop4All_backend.entities.Image;
import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.exceptions.ProductException;
import com.example.shop4All_backend.services.CategoryService;
import com.example.shop4All_backend.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    private final ProductService productService;
    private final CategoryService categoryService;

    // Handle POST requests to /product/add to add a new product
    @PostMapping(value = {"seller/product/add"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Product> addNewProduct(@RequestPart(value = "product", required = false) Product product,
                                                 @RequestPart(value = "imageFile", required = false) MultipartFile[] file) {
        logger.info("Adding new product");
        if (file == null || file[0].getContentType() == null || file[0].getContentType().isEmpty()) {
            logger.error("Invalid image file");
            throw new ProductException("File is empty");
        }
        Set<Image> images = uploadImage(file);
        if (!categoryService.checkCategory(product)) {
            logger.error("Invalid category");
            throw new ProductException("Category does not exist");
        }
        product.setProductImages(images);
        logger.info("Added new product");
        return new ResponseEntity<>(productService.addNewProduct(product), HttpStatus.CREATED);
    }

    //Handle POST request to /product/statusProduct/{poductId} to disable o enable a product on market
    @PostMapping("seller/product/statusProduct/{productId}")
    public ResponseEntity<Product> statusProduct(@PathVariable("productId") Integer productId) {
        return new ResponseEntity<>(productService.statusProduct(productId), HttpStatus.OK);
    }

    //Handle GET request to /product/getAll to get all products
    @GetMapping(value = {"/product/getAll"})
    public ResponseEntity<List<Product>> getAllProducts(@RequestParam(defaultValue = "0") int pageNumber) {
        logger.info("Get all products");
        return new ResponseEntity<>(productService.getAllProducts(pageNumber), HttpStatus.OK);
    }


    //Handle GET request to /product/getByCompanyName to get all products sold by a seller
    @GetMapping(value = {"seller/product/getByCompanyName"})
    public ResponseEntity<List<Product>> getAllProductsByCompany(@RequestParam(defaultValue = "0") int pageNumber) {
        logger.info("Get all products by company with pagination");
        return new ResponseEntity<>(productService.getAllProductsByCompany(pageNumber), HttpStatus.OK);
    }

    @GetMapping(value =  {"/product/getByCategory/{categoryName}"})
    public ResponseEntity<List<Product>> getProductByCategory(@PathVariable("categoryName") String categoryName, @RequestParam(defaultValue = "0") int pageNumber) {
        logger.info("Get all products by category with pagination");
        return new ResponseEntity<>(productService.getProductsByCategory(categoryName, pageNumber), HttpStatus.OK);
    }

    //Handle GET request to /product/getByCompanyName to get all products sold by a seller
    @GetMapping(value = {"seller/product/getAllByCompanyName"})
    public ResponseEntity<List<Product>> getAllProductsByCompany() {
        logger.info("Get all products by company");
        return new ResponseEntity<>(productService.getAllProductsByCompany(), HttpStatus.OK);
    }

    //Handle GET request to /product/getById/{productId} to get a product based on productId
    @GetMapping("/product/getById/{productId}")
    public ResponseEntity<Product> getProductById(@PathVariable("productId") Integer productId) {
        logger.info("Get product by id {}", productId);
        return new ResponseEntity<>(productService.getProductById(productId), HttpStatus.OK);
    }

    @PostMapping("/product/addView/{productId}")
    public ResponseEntity<Product> addView(@PathVariable("productId") Integer productId) {
        logger.info("Add view with id {}", productId);
        return new ResponseEntity<>(productService.addView(productId), HttpStatus.OK);
    }

    //Handle DELETE request to /product/delete/{productId} to delete a product based on productId
    @DeleteMapping(value = {"seller/product/delete/{productId}"})
    public ResponseEntity<Product> deleteProduct(@PathVariable("productId") Integer productId) {
        logger.info("Delete product by id {}", productId);
        return new ResponseEntity<>(productService.deleteProduct(productId), HttpStatus.OK);
    }

    //Handle POST request to /product/checkDiscount to check the fields of a discount
    @PostMapping(value = {"seller/product/checkDiscount"})
    public ResponseEntity<Map<String, String>> checkDiscount(@RequestBody Product product) {
        logger.info("Check discount");
        productService.checkDiscount(product);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Product added to list");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    //Handle POST request to /product/setDiscount to set a discount for a product
    @PostMapping(value = {"seller/product/setDiscount"})
    public ResponseEntity<Product> setDiscount(@RequestBody Product product) {
        logger.info("Set discount");
        return new ResponseEntity<>(productService.setDiscount(product), HttpStatus.CREATED);
    }

    //Handle DELETE request to /product/deleteDiscount/{productId} to delete a discount from a product
    @DeleteMapping(value = {"seller/product/deleteDiscount/{productId}"})
    public ResponseEntity<Product> deleteDiscount(@PathVariable("productId") Integer productId) {
        logger.info("Delete discount");
        return new ResponseEntity<>(productService.deleteDiscount(productId), HttpStatus.ACCEPTED);
    }

    //Handle GET request to /getProductDetails/{isSingleProductCheckout}/{productId} to get product details based on checkout type(cart or single product checkout)
    @GetMapping({"buyer/getProductDetails/{isSingleProductCheckout}/{productId}"})
    public ResponseEntity<List<Product>> getProductDetails(@PathVariable(name = "isSingleProductCheckout") boolean isSingleProductCheckout, @PathVariable(name = "productId") Integer productId) {
        logger.info("Get product details");
        return new ResponseEntity<>(productService.getProductDetails(isSingleProductCheckout, productId), HttpStatus.CREATED);
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
