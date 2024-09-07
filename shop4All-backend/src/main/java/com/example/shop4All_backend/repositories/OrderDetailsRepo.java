package com.example.shop4All_backend.repositories;

import com.example.shop4All_backend.entities.OrderDetails;
import org.springframework.data.repository.CrudRepository;

public interface OrderDetailsRepo extends CrudRepository<OrderDetails, Integer> {
}
