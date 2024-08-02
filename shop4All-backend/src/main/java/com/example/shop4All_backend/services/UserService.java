package com.example.shop4All_backend.services;

import com.example.shop4All_backend.entities.Role;
import com.example.shop4All_backend.entities.User;
import com.example.shop4All_backend.exceptions.RequestException;
import com.example.shop4All_backend.repositories.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //seller registration
    public User registerNewSeller(User seller) {
        validateEmail(seller.getUserEmail());
        validatePassword(seller.getUserPassword());

        Optional<User> existingUser = userRepo.findByUserEmail(seller.getUserEmail());
        if (existingUser.isPresent()) {
            throw new RequestException("User with this email already exists");
        }
        seller.setRole(Role.SELLER);
        seller.setUserIsValid(false);

        seller.setUserPassword(getEncodedPassword(seller.getUserPassword()));
        return userRepo.save(seller);
    }

    //buyer registration
    public User registernewBuyer(User buyer) {
        validateEmail(buyer.getUserEmail());
        validatePassword(buyer.getUserPassword());

        Optional<User> existingUser = userRepo.findByUserEmail(buyer.getUserEmail());
        if (existingUser.isPresent()) {
            throw new RequestException("User with this email already exists");
        }
        buyer.setRole(Role.BUYER);
        buyer.setUserPassword(getEncodedPassword(buyer.getUserPassword()));

        return userRepo.save(buyer);
    }

    //activate seller's account
    public boolean activateAccount(String email) {
        User seller = userRepo.findByUserEmail(email).orElseThrow(() -> new RequestException("User not found"));
        if (seller.isUserIsValid() == false) {
            seller.setUserIsValid(true);
            userRepo.save(seller);
            return true;
        }
        return false;
    }

    //demo for roles and an admin
    public void initRolesandUser() {
        User adminUser = new User();
        adminUser.setUserEmail("admin@gmail.com");
        adminUser.setUserPassword(getEncodedPassword("admin"));
        adminUser.setRole(Role.ADMIN);
        adminUser.setUserIsValid(true);
        userRepo.save(adminUser);

        User sellerUser = new User();
        sellerUser.setUserEmail("seller@gmail.com");
        sellerUser.setUserPassword(getEncodedPassword("seller"));
        sellerUser.setRole(Role.SELLER);
        sellerUser.setUserIsValid(true);
        userRepo.save(sellerUser);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            throw new RequestException("Invalid email format");
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new RequestException("Password must be at least 8 characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new RequestException("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new RequestException("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            throw new RequestException("Password must contain at least one digit");
        }
        if (!password.matches(".*[!@#$%^&*].*")) {
            throw new RequestException("Password must contain at least one special character (!@#$%^&*)");
        }
    }
}
