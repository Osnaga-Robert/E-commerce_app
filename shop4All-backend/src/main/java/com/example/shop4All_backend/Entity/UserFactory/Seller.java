package com.example.shop4All_backend.Entity.UserFactory;

import com.example.shop4All_backend.Entity.Role;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Seller implements User{
    @Id
    private String userEmail;
    private String userFirstName;
    private String userLastName;
    private String userPassword;
    private String userCompanyName;
    private String userCompanyDesciption;
    private boolean userIsValid;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE_SELLER",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<Role> role;

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
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

    public Set<Role> getRole() {
        return role;
    }

    public void setRole(Set<Role> role) {
        this.role = role;
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

    public boolean isUserIsValid() {
        return userIsValid;
    }

    public void setUserIsValid(boolean userIsValid) {
        this.userIsValid = userIsValid;
    }
}
