package com.example.shop4All_backend.repositories;

import com.example.shop4All_backend.entities.User;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Integer> {
    Optional<User> findByUserEmail(String userEmail);
}
