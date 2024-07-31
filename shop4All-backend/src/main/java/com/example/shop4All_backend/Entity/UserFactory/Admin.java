package com.example.shop4All_backend.Entity.UserFactory;

import com.example.shop4All_backend.Entity.Role;
import jakarta.persistence.*;

import java.util.Set;

@Entity
public class Admin implements User{
    @Id
    private String userEmail;
    private String userPassword;
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_ROLE_ADMIN",
            joinColumns = {
                    @JoinColumn(name = "USER_ID")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "ROLE_ID")
            }
    )
    private Set<Role> role;

    @Override
    public Set<Role> getRole() {
        return role;
    }

    @Override
    public void setRole(Set<Role> role) {
        this.role = role;
    }

    @Override
    public String getUserPassword() {
        return userPassword;
    }

    @Override
    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public String getUserEmail() {
        return userEmail;
    }

    @Override
    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
