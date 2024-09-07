package com.example.shop4All_backend.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderInput {
    private String fullName;
    private String fullAddress;
    private String contactNumber;
    private List<OrderProductQuantity> products;
}
