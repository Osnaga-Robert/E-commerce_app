package com.example.shop4All_backend.repositories;

import com.example.shop4All_backend.entities.Category;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface CategoryRepo extends CrudRepository<Category, Integer> {
    Optional<Category> findByCategoryName(String categoryName);
}
