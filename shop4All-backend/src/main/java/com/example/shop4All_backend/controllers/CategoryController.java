package com.example.shop4All_backend.controllers;

import com.example.shop4All_backend.entities.Category;
import com.example.shop4All_backend.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final CategoryService categoryService;

    // Handle POST request to /category/add to add a new category for products
    @PostMapping("admin/category/add")
    public ResponseEntity<Category> addCategory(@RequestBody Category category) {
        logger.info("Adding category: {}", category);
        return new ResponseEntity<>(categoryService.addNewCategory(category), HttpStatus.CREATED);
    }

    // Handle GET request to /category/getAll to get all categories
    @GetMapping("/category/getAll")
    public ResponseEntity<List<Category>> getAllCategories() {
        logger.info("Getting all categories");
        return new ResponseEntity<>(categoryService.getAllCategories(), HttpStatus.OK);
    }

}
