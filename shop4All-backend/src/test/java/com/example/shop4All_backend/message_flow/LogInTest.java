package com.example.shop4All_backend.message_flow;

import com.example.shop4All_backend.dtos.JwtRequest;
import com.example.shop4All_backend.dtos.JwtResponse;
import com.example.shop4All_backend.entities.Role;
import com.example.shop4All_backend.entities.User;
import com.example.shop4All_backend.exceptions.LogInException;
import com.example.shop4All_backend.repositories.UserRepo;
import com.example.shop4All_backend.services.JwtService;
import com.example.shop4All_backend.util.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class LogInTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserRepo userRepo;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private AuthenticationManager authenticationManager;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateJwtTokenSuccess() throws LogInException {
        User user = User.builder()
                .userEmail("buyer@gmail.com")
                .userPassword("Password1!")
                .userFirstName("firstName1")
                .userLastName("lastName1")
                .role(Role.BUYER)
                .build();

        JwtRequest jwtRequest = new JwtRequest("buyer@gmail.com", "Password1!");

        when(userRepo.findByUserEmail(user.getUserEmail())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtUtil.generateToken(any(UserDetails.class))).thenReturn("jwtToken");

        JwtResponse jwtResponse = jwtService.createJwtToken(jwtRequest);

        assertNotNull(jwtResponse);
        assertEquals("jwtToken", jwtResponse.getJwtToken());
        assertEquals(user, jwtResponse.getUser());
    }

    @Test
    public void testCreateJwtTokenInvalidCredentials() {
        User user = User.builder()
                .userEmail("buyer@gmail.com")
                .userPassword("Password1!")
                .userFirstName("firstName1")
                .userLastName("lastName1")
                .role(Role.BUYER)
                .build();

        JwtRequest jwtRequest = new JwtRequest("user@example.com", "wrongPassword");

        when(userRepo.findByUserEmail(user.getUserEmail())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        LogInException thrown = assertThrows(LogInException.class, () -> {
            jwtService.createJwtToken(jwtRequest);
        });

        assertEquals("Bad credentials for user", thrown.getMessage());
    }

    @Test
    public void testLoadUserByUsername() {
        User user = User.builder()
                .userEmail("buyer@gmail.com")
                .userPassword("Password1!")
                .userFirstName("firstName1")
                .userLastName("lastName1")
                .role(Role.BUYER)
                .build();

        when(userRepo.findByUserEmail(user.getUserEmail())).thenReturn(Optional.of(user));

        UserDetails userDetails = jwtService.loadUserByUsername(user.getUserEmail());

        assertNotNull(userDetails);
        assertEquals(user.getUserEmail(), userDetails.getUsername());
        assertEquals(user.getUserPassword(), userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_BUYER")));
    }

    @Test
    public void testLoadUserByUsernameUserNotFound() {
        when(userRepo.findByUserEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        LogInException thrown = assertThrows(LogInException.class, () -> {
            jwtService.loadUserByUsername("nonexistent@example.com");
        });

        assertEquals("User not found", thrown.getMessage());
    }
}
