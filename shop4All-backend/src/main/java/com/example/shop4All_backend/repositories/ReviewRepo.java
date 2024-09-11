package com.example.shop4All_backend.repositories;

import com.example.shop4All_backend.entities.Review;
import org.springframework.data.repository.CrudRepository;

public interface ReviewRepo extends CrudRepository<Review, Integer> {
}
