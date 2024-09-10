package com.example.shop4All_backend.services;

import com.example.shop4All_backend.entities.Category;
import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.entities.Role;
import com.example.shop4All_backend.entities.User;
import com.example.shop4All_backend.exceptions.RegisterException;
import com.example.shop4All_backend.repositories.CategoryRepo;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.repositories.UserRepo;
import com.nimbusds.jose.jwk.source.RateLimitReachedException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;
    private final EmailService emailService;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    final String passwordRegex = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[!@#$%^&*])[A-Za-z\\d!@#$%^&*]{8,}$";
    final String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    //seller registration
    public User registerNewSeller(User seller) {
        validateCredentials(seller, Role.SELLER);
        Optional<User> existingUser = userRepo.findByUserEmail(seller.getUserEmail());
        if (existingUser.isPresent()) {
            logger.error("User already exists");
            throw new RegisterException("User with this email already exists");
        }
        seller.setRole(Role.SELLER);
        seller.setUserIsValid(false);
        emailService.sendEmail(seller.getUserEmail(), "Shop4All", "Hello to our app. You will get your response as soon as posibble");

        seller.setUserPassword(getEncodedPassword(seller.getUserPassword()));
        logger.info("New seller: " + seller);
        return userRepo.save(seller);
    }

    //buyer registration
    public User registerBuyer(User buyer) {
        validateCredentials(buyer, Role.BUYER);

        Optional<User> existingUser = userRepo.findByUserEmail(buyer.getUserEmail());
        if (existingUser.isPresent()) {
            logger.error("User already exists");
            throw new RegisterException("User with this email already exists");
        }
        buyer.setRole(Role.BUYER);
        buyer.setUserPassword(getEncodedPassword(buyer.getUserPassword()));
        emailService.sendEmail(buyer.getUserEmail(), "Shop4All", "Hello to our app");

        logger.info("New buyer: " + buyer);
        return userRepo.save(buyer);
    }

    //activate seller's account
    public boolean activateAccount(String email) {
        User seller = userRepo.findByUserEmail(email).orElseThrow(() -> new RegisterException("User not found"));
        if (!seller.isUserIsValid()) {
            seller.setUserIsValid(true);
            userRepo.save(seller);
            logger.info("User activated");
            emailService.sendEmail(seller.getUserEmail(), "Shop4All", "Your account has been aproved");
            return true;
        }
        emailService.sendEmail(seller.getUserEmail(), "Shop4All", "Your account has been refused");
        logger.info("User refused");
        return false;
    }

    //demo for roles and an admin
    public void initRolesandUser() {
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    //pattern to validate the email format
    private void validateEmail(String email) {
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches())
            return;
        logger.error("Invalid email");
        throw new RegisterException("Invalid email format");
    }

    //pattern to validate the password format
    private void validatePassword(String password) {
        Pattern pattern = Pattern.compile(passwordRegex);
        Matcher matcher = pattern.matcher(password);
        if (!matcher.matches()) {
            logger.error("Invalid password");
            throw new RegisterException("Your password should be at least 8 characters " +
                    "long and contain at least one digit, one upperrcase letter, one lowercase letter " +
                    "and one special character(!@#$%^&*)");
        }
    }

    //check the user's fields
    private <T extends User> void validateCredentials(T user, Role role) {
        if (role == Role.SELLER) {
            if (user.getUserCompanyDesciption() == null || user.getUserCompanyName() == null ||
                    user.getUserFirstName() == null || user.getUserLastName() == null ||
                    user.getUserEmail() == null || user.getUserPassword() == null ||
                    user.getUserFirstName().isEmpty() || user.getUserLastName().isEmpty() ||
                    user.getUserEmail().isEmpty() || user.getUserPassword().isEmpty() ||
                    user.getUserCompanyName().isEmpty() || user.getUserCompanyDesciption().isEmpty()) {
                logger.error("Invalid credentials");
                throw new RegisterException("All credentials are required for a seller");
            }
        } else if (role == Role.BUYER) {
            if (user.getUserFirstName() == null || user.getUserLastName() == null ||
                    user.getUserEmail() == null || user.getUserPassword() == null ||
                    user.getUserFirstName().isEmpty() || user.getUserLastName().isEmpty() ||
                    user.getUserEmail().isEmpty() || user.getUserPassword().isEmpty()) {
                logger.error("Invalid credentials");
                throw new RegisterException("All credentials are required for a buyer");
            }
        }
        validateEmail(user.getUserEmail());
        validatePassword(user.getUserPassword());
    }

    //get the account that are not validated by the admin
    public List<User> getNonActivatedAccounts() {
        return userRepo.findByUserIsValid(false);
    }

    //decline an account by admin
    @Transactional
    public String declineAccount(String email) {
        userRepo.deleteByUserEmail(email);
        return "Account deleted";
    }
}
