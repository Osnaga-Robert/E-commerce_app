package com.example.shop4All_backend.message_flow;

import com.example.shop4All_backend.entities.Role;
import com.example.shop4All_backend.entities.User;
import com.example.shop4All_backend.exceptions.RegisterException;
import com.example.shop4All_backend.repositories.UserRepo;
import com.example.shop4All_backend.services.EmailService;
import com.example.shop4All_backend.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class RegisterTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    public RegisterTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterBuyerSuccess() {
        User buyer = User.builder()
                .userEmail("buyer@gmail.com")
                .userPassword("Password1!")
                .userFirstName("firstName1")
                .userLastName("lastName1")
                .build();

        when(userRepo.findByUserEmail(buyer.getUserEmail())).thenReturn(Optional.empty());

        User savedBuyer = new User();
        savedBuyer.setUserEmail(buyer.getUserEmail());
        savedBuyer.setUserPassword("encodedPassword");
        savedBuyer.setRole(Role.BUYER);

        when(userRepo.save(buyer)).thenReturn(savedBuyer);

        User result = userService.registerBuyer(buyer);

        assertNotNull(result);
        assertEquals("buyer@gmail.com", result.getUserEmail());
        assertEquals("encodedPassword", result.getUserPassword());
        assertEquals(Role.BUYER, result.getRole());
    }

    @Test
    public void testRegisterBuyerFailureWhenEmailExists() {
        User buyer = User.builder()
                .userEmail("buyer@gmail.com")
                .userPassword("Password1!")
                .userFirstName("firstName1")
                .userLastName("lastName1")
                .build();

        when(userRepo.findByUserEmail(buyer.getUserEmail())).thenReturn(Optional.of(buyer));

        RegisterException thrown = assertThrows(RegisterException.class, () -> {
            userService.registerBuyer(buyer);
        });

        assertEquals("User with this email already exists", thrown.getMessage());
    }

    @Test
    public void testRegisterBuyerFailureInvalidEmail() {
        User buyer = User.builder()
                .userEmail("buyer")
                .userPassword("Password1!")
                .userFirstName("firstName1")
                .userLastName("lastName1")
                .build();

        RegisterException thrown = assertThrows(RegisterException.class, () -> {
            userService.registerBuyer(buyer);
        });

        assertEquals("Invalid email format", thrown.getMessage());
    }

    @Test
    public void testRegisterBuyerFailureInvalidPassword() {
        User buyer = User.builder()
                .userEmail("buyer@gmail.com")
                .userPassword("buyer")
                .userFirstName("firstName1")
                .userLastName("lastName1")
                .build();

        RegisterException thrown = assertThrows(RegisterException.class, () -> {
            userService.registerBuyer(buyer);
        });

        assertEquals("Your password should be at least 8 characters long and contain at least one digit, one upperrcase letter, one lowercase letter and one special character(!@#$%^&*)", thrown.getMessage());
    }
}
