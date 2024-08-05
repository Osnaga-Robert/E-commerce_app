package com.example.shop4All_backend.services;

import com.example.shop4All_backend.entities.Category;
import com.example.shop4All_backend.entities.Role;
import com.example.shop4All_backend.entities.User;
import com.example.shop4All_backend.exceptions.RegisterException;
import com.example.shop4All_backend.repositories.CategoryRepo;
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
    @Autowired
    private CategoryRepo categoryRepo;

    //seller registration
    public User registerNewSeller(User seller) {
        validateAllCredentialsSeller(seller);
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
    public User registerBuyer(User buyer) {
        validateAllCredentialsBuyer(buyer);
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
        User seller = userRepo.findByUserEmail(email).orElseThrow(() -> new RegisterException("User not found"));
        if (!seller.isUserIsValid()) {
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
        sellerUser.setUserCompanyName("COMPANY2024");
        userRepo.save(sellerUser);

        Category category = new Category();
        category.setCategoryName("categoryName1");
        category.setCategoryDescription("categoryDesc1");
        categoryRepo.save(category);
    }

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    //pattern to validate the email format
    private void validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            throw new RegisterException("Invalid email format");
        }
    }

    //pattern to validate the password format
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

    //check the essential credentials for a seller account
    private void validateAllCredentialsSeller(User seller) {
        if (seller.getUserCompanyDesciption() == null || seller.getUserCompanyName() == null ||
                seller.getUserFirstName() == null || seller.getUserLastName() == null ||
                seller.getUserEmail() == null || seller.getUserPassword() == null ||
                seller.getUserFirstName().isEmpty() || seller.getUserLastName().isEmpty() ||
                seller.getUserEmail().isEmpty() || seller.getUserPassword().isEmpty() ||
                seller.getUserCompanyName().isEmpty() || seller.getUserCompanyDesciption().isEmpty()) {
            throw new RegisterException("All credentials are required");
        }
    }

    //check the essential credentials for a buyer account
    private void validateAllCredentialsBuyer(User seller) {
        if (seller.getUserFirstName() == null || seller.getUserLastName() == null ||
                seller.getUserFirstName().isEmpty() || seller.getUserLastName().isEmpty()) {
            throw new RegisterException("All credentials are required");
        }
    }
}
