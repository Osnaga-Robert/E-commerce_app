package com.example.shop4All_backend.repositories;

import com.example.shop4All_backend.entities.OrderDetails;
import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderDetailsRepo extends CrudRepository<OrderDetails, Integer> {
    public List<OrderDetails> findByUser(User user);
    public List<OrderDetails> findByProduct(Product product);
    public void deleteByProduct(Product product);
}
