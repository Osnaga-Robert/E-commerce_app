package com.example.shop4All_backend.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import lombok.Setter;

@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Setter
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userPassword;
    private String userCompanyName;
    private String userCompanyDesciption;
    private Role role;
    private boolean userIsValid;

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public String getUserCompanyName() {
        return userCompanyName;
    }

    public void setUserCompanyName(String userCompanyName) {
        this.userCompanyName = userCompanyName;
    }

    public String getUserCompanyDesciption() {
        return userCompanyDesciption;
    }

    public void setUserCompanyDesciption(String userCompanyDesciption) {
        this.userCompanyDesciption = userCompanyDesciption;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isUserIsValid() {
        return userIsValid;
    }

    public void setUserIsValid(boolean userIsValid) {
        this.userIsValid = userIsValid;
    }
}
