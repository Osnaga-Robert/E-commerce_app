package com.example.shop4All_backend.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

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
    private Double orderPrice;
    private Integer orderQuantity;
    private LocalDate orderDate;
    @ManyToOne
    private Product product;
    @ManyToOne
    private User user;
}
