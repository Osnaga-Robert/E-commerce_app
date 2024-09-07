package com.example.shop4All_backend.entities;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderProductQuantity {
    private Integer productId;
    private Integer quantity;
}
