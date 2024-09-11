package com.example.shop4All_backend.repositories;

import com.example.shop4All_backend.entities.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface ProductRepo extends CrudRepository<Product, Integer> {
    Optional<Product> findByProductName(String productName);
    Page<Product> findByIsActiveTrue(Pageable pageable);
    Page<Product> findByCompanySeller(String companySeller, Pageable pageable);
}
