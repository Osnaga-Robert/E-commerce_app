package com.example.shop4All_backend.repositories;

import com.example.shop4All_backend.entities.Cart;
import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepo extends CrudRepository<Cart, Integer> {
    public List<Cart> findByUser(User user);
    public void deleteByProduct(Product product);
}
