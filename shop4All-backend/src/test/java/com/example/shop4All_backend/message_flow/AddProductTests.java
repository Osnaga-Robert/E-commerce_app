package com.example.shop4All_backend.message_flow;

import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.entities.User;
import com.example.shop4All_backend.exceptions.ProductException;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.repositories.UserRepo;
import com.example.shop4All_backend.services.ProductService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class AddProductTests {

    @Mock
    private ProductRepo productRepo;

    @Mock
    private UserRepo userRepo;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddNewProductSuccess() {
        Product product = new Product();
        product.setProductName("Test Product");
        product.setProductDescription("Test Description");
        product.setProductPrice(100.0);
        product.setProductQuantity(10);

        User user = new User();
        user.setUserEmail("seller@example.com");
        user.setUserCompanyName("Test Company");

        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("seller@example.com");

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null));

        when(userRepo.findByUserEmail("seller@example.com")).thenReturn(Optional.of(user));
        when(productRepo.save(any(Product.class))).thenReturn(product);

        Product result = productService.addNewProduct(product);

        assertNotNull(result, "Product should not be null");
        assertTrue(result.isActive(), "Product should be active");
        assertEquals("Test Company", result.getCompanySeller(), "Product's company seller should match");
    }

    @Test
    public void testAddNewProductInvalidCredentials() {
        Product product = new Product();
        product.setProductName("");
        product.setProductDescription("Test Description");
        product.setProductPrice(100.0);
        product.setProductQuantity(10);

        assertThrows(ProductException.class, () -> productService.addNewProduct(product), "Invalid credentials");
    }
}
