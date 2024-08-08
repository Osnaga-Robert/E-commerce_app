package com.example.shop4All_backend.controllers;

import com.example.shop4All_backend.entities.Image;
import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.exceptions.UserException;
import com.example.shop4All_backend.services.CategoryService;
import com.example.shop4All_backend.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@RestController
public class ProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    // Handle POST requests to /product/add to add a new product
    @PostMapping(value = {"/product/add"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Product> addNewProduct(@RequestPart("product") Product product,
                                                 @RequestPart("imageFile") MultipartFile[] file) throws IOException {
        Set<Image> images = uploadImage(file);
        if (!categoryService.checkCategory(product))
            throw new UserException("Category does not exist");
        product.setProductImages(images);
        return new ResponseEntity<>(productService.addNewProduct(product), HttpStatus.CREATED);
    }

    //format images to store in products object
    private Set<Image> uploadImage(MultipartFile[] multipartFile) throws IOException {
        Set<Image> images = new HashSet<>();
        for (MultipartFile file : multipartFile) {
            Image image = new Image(file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes()
            );
            images.add(image);
        }
        return images;
    }
}
