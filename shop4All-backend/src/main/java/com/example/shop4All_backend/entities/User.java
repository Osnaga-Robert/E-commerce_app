package com.example.shop4All_backend.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userPassword;
    private String userCompanyName;
    private String userCompanyDesciption;
    private Role role;
    private boolean userIsValid;
}
