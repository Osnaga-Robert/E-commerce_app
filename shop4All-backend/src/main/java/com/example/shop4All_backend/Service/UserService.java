package com.example.shop4All_backend.Service;

import com.example.shop4All_backend.Entity.Role;
import com.example.shop4All_backend.Entity.User;
import com.example.shop4All_backend.Repository.RoleRepo;
import com.example.shop4All_backend.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    public User registerNewUser(User user) {
        return userRepo.save(user);
    }

    public void initRolesandUser(){
        Role adminRole = new Role();
        adminRole.setRoleName("admin");
        adminRole.setRoleDescription("Admin role");
        roleRepo.save(adminRole);

        Role sellerRole = new Role();
        sellerRole.setRoleName("seller");
        sellerRole.setRoleDescription("Seller role");
        roleRepo.save(sellerRole);

        User adminUser = new User();
        adminUser.setUserEmail("admin@gmail.com");
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");
        adminUser.setUserPassword("admin");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userRepo.save(adminUser);

        User sellerUser = new User();
        sellerUser.setUserEmail("seller@gmail.com");
        sellerUser.setUserFirstName("seller");
        sellerUser.setUserLastName("seller");
        sellerUser.setUserPassword("seller");
        Set<Role> sellerRoles = new HashSet<>();
        sellerRoles.add(sellerRole);
        sellerUser.setRole(sellerRoles);
        userRepo.save(sellerUser);
    }

}
