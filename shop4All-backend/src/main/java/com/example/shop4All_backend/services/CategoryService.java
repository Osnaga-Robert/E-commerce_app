package com.example.shop4All_backend.services;

import com.example.shop4All_backend.entities.Category;
import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.exceptions.UserException;
import com.example.shop4All_backend.repositories.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import java.util.HashSet;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RequiredArgsConstructor
@Service
public class CategoryService {

    private final CategoryRepo categoryRepo;

    //add a category of products
    public Category addNewCategory(Category category) {
        if (categoryRepo.findByCategoryName(category.getCategoryName()).isPresent())
            throw new UserException("Category already exists");
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
        return StreamSupport.stream(categories.spliterator(), false)
                .collect(Collectors.toList());
    }
}
