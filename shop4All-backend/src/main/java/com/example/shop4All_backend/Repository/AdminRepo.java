package com.example.shop4All_backend.Repository;

import com.example.shop4All_backend.Entity.UserFactory.Admin;
import org.springframework.data.repository.CrudRepository;

public interface AdminRepo extends CrudRepository<Admin, String> {
}
