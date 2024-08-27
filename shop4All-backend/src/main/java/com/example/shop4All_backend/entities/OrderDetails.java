package com.example.shop4All_backend.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer orderId;
    private String orderFullName;
    private String orderFullAddress;
    private String orderContactNumber;
    private String orderStatus;
    private Double orderAmount;
    @ManyToOne
    private Product product;
    @ManyToOne
    private User user;
}
