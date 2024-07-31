package com.example.shop4All_backend.Repository;

import com.example.shop4All_backend.Entity.UserFactory.Seller;
import org.springframework.data.repository.CrudRepository;

public interface SellerRepo extends CrudRepository<Seller, String> {
}
