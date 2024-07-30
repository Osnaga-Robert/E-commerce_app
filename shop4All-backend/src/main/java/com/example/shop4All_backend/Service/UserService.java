package com.example.shop4All_backend.Service;

import com.example.shop4All_backend.Entity.Role;
import com.example.shop4All_backend.Entity.User;
import com.example.shop4All_backend.Repository.RoleRepo;
import com.example.shop4All_backend.Repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //user registration(seller in this case)
    public User registernewSeller(User user) throws Exception{
        Optional<User> existingUser = userRepo.findById(user.getUserEmail());
        if (existingUser.isPresent()) {
            System.out.println("DSADASDa");
            // Handle the case where the user already exists
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }

        Role role = roleRepo.findById("Seller").orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRole(roles);

        user.setUserPassword(getEncodedPassword(user.getUserPassword()));
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
        adminUser.setUserPassword(getEncodedPassword("admin"));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userRepo.save(adminUser);


    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

}
