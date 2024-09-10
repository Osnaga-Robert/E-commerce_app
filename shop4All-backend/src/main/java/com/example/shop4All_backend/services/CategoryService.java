package com.example.shop4All_backend.services;

import com.example.shop4All_backend.entities.Category;
import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.exceptions.UserException;
import com.example.shop4All_backend.repositories.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
@RequiredArgsConstructor
public class CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryService.class);
    private final CategoryRepo categoryRepo;

    //add a category of products
    public Category addNewCategory(Category category) {
        if (categoryRepo.findByCategoryName(category.getCategoryName()).isPresent()) {
            logger.error("Category with name {} already exists", category.getCategoryName());
            throw new UserException("Category already exists");
        }
        logger.info("Adding new category {}", category.getCategoryName());
        return categoryRepo.save(category);
    }

    //check if the category of a product exists in the database
    public boolean checkCategory(Product product) {
        Set<Category> categories = product.getProductCategory();
        for (Category category : categories) {
            Optional<Category> categoryOptional = categoryRepo.findByCategoryName(category.getCategoryName());
            if (categoryOptional.isPresent()) {
                HashSet<Category> categorySet = new HashSet<>();
                categorySet.add(categoryOptional.get());
                product.setProductCategory(categorySet);
                return true;
            }
        }
        return false;
    }

    //get all categories added by admin
    public List<Category> getAllCategories() {
        Iterable<Category> categories = categoryRepo.findAll();
        logger.info("Getting all categories");
        return StreamSupport.stream(categories.spliterator(), false)
                .collect(Collectors.toList());
    }
}
