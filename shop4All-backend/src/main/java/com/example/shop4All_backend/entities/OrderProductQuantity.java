package com.example.shop4All_backend.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class OrderProductQuantity {
    private Integer productId;
    private Integer quantity;
}
