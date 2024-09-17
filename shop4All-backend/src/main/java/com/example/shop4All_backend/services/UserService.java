package com.example.shop4All_backend.services;

import com.example.shop4All_backend.entities.*;
import com.example.shop4All_backend.exceptions.RegisterException;
import com.example.shop4All_backend.repositories.CategoryRepo;
import com.example.shop4All_backend.repositories.OrderDetailsRepo;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.repositories.UserRepo;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
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
    private final OrderDetailsRepo orderDetailsRepo;

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
        Path SamsungImage1 = Paths.get("Samsung1.jpg");
        Path SamsungImage2 = Paths.get("Samsung2.webp");
        Path IphoneImage1 = Paths.get("Iphone1.jpeg");
        Path IphoneImage2 = Paths.get("Iphone2.jpeg");

        Category samsung = new Category();
        samsung.setCategoryDescription("SAMSUNG");
        samsung.setCategoryName("SAMSUNG");
        categoryRepo.save(samsung);

        Category apple = new Category();
        apple.setCategoryDescription("APPLE");
        apple.setCategoryName("APPLE");
        categoryRepo.save(apple);

        Category huawei = new Category();
        huawei.setCategoryDescription("HUAWEI");
        huawei.setCategoryName("HUAWEI");
        categoryRepo.save(huawei);

        User seller1 = new User();
        seller1.setUserEmail("seller1@gmail.com");
        seller1.setUserPassword(getEncodedPassword("seller"));
        seller1.setRole(Role.SELLER);
        seller1.setUserIsValid(true);
        seller1.setUserCompanyName("SAMSUNG");
        userRepo.save(seller1);

        User seller2 = new User();
        seller2.setUserEmail("seller2@gmail.com");
        seller2.setUserPassword(getEncodedPassword("seller"));
        seller2.setRole(Role.SELLER);
        seller2.setUserIsValid(true);
        seller2.setUserCompanyName("APPLE");
        userRepo.save(seller2);

        User buyer1 = new User();
        buyer1.setUserEmail("buyer1@gmail.com");
        buyer1.setUserPassword(getEncodedPassword("buyer"));
        buyer1.setRole(Role.BUYER);
        buyer1.setUserIsValid(true);
        userRepo.save(buyer1);

        User buyer2 = new User();
        buyer2.setUserEmail("buyer2@gmail.com");
        buyer2.setUserPassword(getEncodedPassword("buyer"));
        buyer2.setRole(Role.BUYER);
        buyer2.setUserIsValid(true);
        userRepo.save(buyer2);

        User admin = new User();
        admin.setUserEmail("admin@gmail.com");
        admin.setUserPassword(getEncodedPassword("admin"));
        admin.setRole(Role.ADMIN);
        admin.setUserIsValid(true);
        userRepo.save(admin);

        Random random = new Random();

        for (int i = 1; i <= 100; i++) {
            Product product = new Product();

            product.setProductPrice(Math.round(random.nextDouble() * 1000 * 100) / 100.0);
            product.setProductQuantity(Math.abs(random.nextInt(1000)));
            product.setActive(true);
            product.setViews(Math.abs(random.nextInt(1000)));
            product.setProductDiscounted(0.0);

            int rand = Math.abs(random.nextInt() % 2);
            if(rand == 0) {
                product.setProductName("Samsung Galaxy S" + i);
                product.setProductDescription("This is a phone created by SAMSUNG");
                product.setCompanySeller("SAMSUNG");
                HashSet<Category> categories = new HashSet<>();
                categories.add(samsung);
                product.setProductCategory(categories);

                Set<Image> images = new HashSet<>();
                try {
                    byte[] imageBytes1 = Files.readAllBytes(SamsungImage1);
                    byte[] imageBytes2 = Files.readAllBytes(SamsungImage2);

                    Image image1 = new Image();
                    image1.setName("image_" + i + "_1.jpg");
                    image1.setType("image/jpeg");
                    image1.setImageByte(imageBytes1);

                    Image image2 = new Image();
                    image2.setName("image_" + i + "_2.jpg");
                    image2.setType("image/jpeg");
                    image2.setImageByte(imageBytes2);

                    images.add(image1);
                    images.add(image2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                product.setProductImages(images);
                productRepo.save(product);
            }
            else{
                product.setProductName("Iphone " + i);
                product.setProductDescription("This is a phone created by APPLE");
                product.setCompanySeller("APPLE");
                HashSet<Category> categories = new HashSet<>();
                categories.add(apple);
                product.setProductCategory(categories);

                Set<Image> images = new HashSet<>();
                try {
                    byte[] imageBytes1 = Files.readAllBytes(IphoneImage1);
                    byte[] imageBytes2 = Files.readAllBytes(IphoneImage2);

                    Image image1 = new Image();
                    image1.setName("image_" + i + "_1.jpg");
                    image1.setType("image/jpeg");
                    image1.setImageByte(imageBytes1);

                    Image image2 = new Image();
                    image2.setName("image_" + i + "_2.jpg");
                    image2.setType("image/jpeg");
                    image2.setImageByte(imageBytes2);

                    images.add(image1);
                    images.add(image2);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                product.setProductImages(images);
                productRepo.save(product);
            }
        }

        LocalDate startDate = LocalDate.of(LocalDate.now().getYear(), 1, 1);

        // Get today's date
        LocalDate endDate = LocalDate.now();

        // Calculate the number of days between the start date and today
        long daysBetween = ChronoUnit.DAYS.between(startDate, endDate);

        for(int i = 1 ; i <= 200 ; i++){
            OrderDetails orderDetails = new OrderDetails();
            orderDetails.setOrderFullName("Order " + i);
            orderDetails.setOrderFullAddress("Address " + i);
            orderDetails.setOrderContactNumber("Contact " + i);
            int rand = Math.abs(random.nextInt() % 2);
            if(rand == 0) {
                orderDetails.setOrderStatus("Placed");
            }
            else{
                orderDetails.setOrderStatus("Delivered");
            }
            rand = Math.abs(random.nextInt() % 100) + 1;
            Product product = productRepo.findById(rand).get();
            orderDetails.setProduct(product);
            orderDetails.setOrderPrice(product.getProductPrice());
            orderDetails.setOrderQuantity((random.nextInt() % 3) + 1);
            orderDetails.setOrderAmount(product.getProductPrice() * orderDetails.getOrderQuantity());
            long randomDays = ThreadLocalRandom.current().nextLong(0, daysBetween + 1);
            LocalDate randomDate = startDate.plusDays(randomDays);
            orderDetails.setOrderDate(randomDate);
            rand = Math.abs(random.nextInt() % 2);
            if(rand == 0) {
                orderDetails.setUser(buyer1);
            }
            else{
                orderDetails.setUser(buyer2);
            }
            orderDetailsRepo.save(orderDetails);
        }
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
