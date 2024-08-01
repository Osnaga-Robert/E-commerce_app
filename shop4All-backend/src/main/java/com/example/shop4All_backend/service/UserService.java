package com.example.shop4All_backend.service;

import com.example.shop4All_backend.entity.Role;
import com.example.shop4All_backend.entity.User;
import com.example.shop4All_backend.exceptions.ActivateAccountException;
import com.example.shop4All_backend.exceptions.RegisterException;
import com.example.shop4All_backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //seller registration
    public User registerNewSeller(User seller) throws Exception {
        validateEmail(seller.getUserEmail());
        validatePassword(seller.getUserPassword());

        Optional<User> existingUser = userRepo.findByUserEmail(seller.getUserEmail());
        if (existingUser.isPresent()) {
            throw new RegisterException("User with this email already exists");
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
            throw new RegisterException("User with this email already exists");
        }

        buyer.setRole(Role.BUYER);

        buyer.setUserPassword(getEncodedPassword(buyer.getUserPassword()));
        return userRepo.save(buyer);
    }

    //activate seller's account
    public boolean activateAccount(String email) {
        User seller = userRepo.findByUserEmail(email).orElseThrow(() -> new ActivateAccountException("User not found"));
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
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            throw new RegisterException("Invalid email format");
        }
    }

    private void validatePassword(String password) {
        if (password.length() < 8) {
            throw new RegisterException("Password must be at least 8 characters long");
        }
        if (!password.matches(".*[A-Z].*")) {
            throw new RegisterException("Password must contain at least one uppercase letter");
        }
        if (!password.matches(".*[a-z].*")) {
            throw new RegisterException("Password must contain at least one lowercase letter");
        }
        if (!password.matches(".*\\d.*")) {
            throw new RegisterException("Password must contain at least one digit");
        }
        if (!password.matches(".*[!@#$%^&*].*")) {
            throw new RegisterException("Password must contain at least one special character (!@#$%^&*)");
        }
    }
}
