package com.example.shop4All_backend.Entity.UserFactory;

public class AccountFactory {
    public static User createUser(String role){
        if(role == null)
            throw new IllegalArgumentException("Role cannot be null");
        return switch (role.toLowerCase()) {
            case "admin" -> new Admin();
            case "seller" -> new Seller();
            case "buyer" -> new Buyer();
            default -> throw new IllegalArgumentException("Invalid role");
        };
    }
}
