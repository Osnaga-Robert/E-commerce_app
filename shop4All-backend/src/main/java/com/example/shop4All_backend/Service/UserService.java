package com.example.shop4All_backend.Service;

import com.example.shop4All_backend.Entity.Role;
import com.example.shop4All_backend.Entity.UserFactory.Admin;
import com.example.shop4All_backend.Entity.UserFactory.Buyer;
import com.example.shop4All_backend.Entity.UserFactory.Seller;
import com.example.shop4All_backend.Repository.AdminRepo;
import com.example.shop4All_backend.Repository.BuyerRepo;
import com.example.shop4All_backend.Repository.RoleRepo;
import com.example.shop4All_backend.Repository.SellerRepo;
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
    private SellerRepo sellerRepo;

    @Autowired
    private BuyerRepo buyerRepo;

    @Autowired
    private AdminRepo adminRepo;

    @Autowired
    private RoleRepo roleRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //seller registration
    public Seller registerNewSeller(Seller seller) throws Exception{
        Optional<Seller> existingUser = sellerRepo.findById(seller.getUserEmail());
        if (existingUser.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }

        Role role = roleRepo.findById("Seller").orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        seller.setRole(roles);
        seller.setUserIsValid(false);

        seller.setUserPassword(getEncodedPassword(seller.getUserPassword()));
        return sellerRepo.save(seller);
    }

    //buyer registration
    public Buyer registernewBuyer(Buyer buyer) {
        Optional<Buyer> existingUser = buyerRepo.findById(buyer.getUserEmail());
        if (existingUser.isPresent()) {
            // Handle the case where the user already exists
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with this email already exists");
        }

        Role role = roleRepo.findById("Buyer").orElseThrow(() -> new RuntimeException("Role not found"));
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        buyer.setRole(roles);

        buyer.setUserPassword(getEncodedPassword(buyer.getUserPassword()));
        return buyerRepo.save(buyer);
    }

    //activate seller's account
    public boolean activateAccount(String email){
        Seller seller = sellerRepo.findById(email).orElseThrow(() -> new RuntimeException("User not found"));
        if (seller.isUserIsValid() == false){
            seller.setUserIsValid(true);
            sellerRepo.save(seller);
            return true;
        }
        return false;
    }

    //demo for roles and an admin
    public void initRolesandUser(){
        Role adminRole = new Role();
        adminRole.setRoleName("admin");
        adminRole.setRoleDescription("Admin role");
        roleRepo.save(adminRole);

        Role sellerRole = new Role();
        sellerRole.setRoleName("seller");
        sellerRole.setRoleDescription("Seller role");
        roleRepo.save(sellerRole);

        Role buyerRole = new Role();
        buyerRole.setRoleName("buyer");
        buyerRole.setRoleDescription("Buyer role");
        roleRepo.save(buyerRole);

        Admin adminUser = new Admin();
        adminUser.setUserEmail("admin@gmail.com");
        adminUser.setUserPassword(getEncodedPassword("admin"));
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        adminRepo.save(adminUser);


    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }


}
