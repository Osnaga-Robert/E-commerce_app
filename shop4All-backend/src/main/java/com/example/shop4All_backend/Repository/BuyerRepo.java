package com.example.shop4All_backend.Repository;

import com.example.shop4All_backend.Entity.UserFactory.Buyer;
import org.springframework.data.repository.CrudRepository;

public interface BuyerRepo extends CrudRepository<Buyer, String> {
}
