package com.example.shop4All_backend.Repository;

import com.example.shop4All_backend.Entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, String> {
}
