package com.example.shop4All_backend.Entity.UserFactory;

import com.example.shop4All_backend.Entity.Role;

import java.util.Set;

public interface User {
    String getUserEmail();

    void setUserEmail(String userEmail);

    String getUserPassword();

    void setUserPassword(String userPassword);

    public Set<Role> getRole();

    public void setRole(Set<Role> role);
}
