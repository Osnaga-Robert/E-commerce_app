package com.example.shop4All_backend.repository;

import com.example.shop4All_backend.entity.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Integer> {
    Optional<User> findByUserEmail(String userEmail);
}
