package com.example.shop4All_backend.message_flow;

import com.example.shop4All_backend.configurations.JwtRequestFilter;
import com.example.shop4All_backend.entities.Cart;
import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.entities.User;
import com.example.shop4All_backend.exceptions.ProductException;
import com.example.shop4All_backend.repositories.CartRepo;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.repositories.UserRepo;
import com.example.shop4All_backend.services.CartService;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartTest {

    @InjectMocks
    private CartService cartService;

    @Mock
    private CartRepo cartRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private UserRepo userRepo;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        JwtRequestFilter.CURRENT_USER = "user@example.com";
    }

    @Test
    public void testAddToCartSuccess() {
        Integer productId = 1;
        Product product = new Product();
        product.setProductId(productId);

        User buyer = new User();
        buyer.setUserEmail("user@example.com");

        Cart cart = new Cart();
        Set<User> buyers = new HashSet<>();
        buyers.add(buyer);
        Set<Product> products = new HashSet<>();
        products.add(product);
        cart.setUser(buyers);
        cart.setProduct(products);

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        when(userRepo.findByUserEmail("user@example.com")).thenReturn(Optional.of(buyer));
        when(cartRepo.findByUser(buyer)).thenReturn(Collections.emptyList());
        when(cartRepo.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.addToCart(productId);

        assertNotNull(result);
        assertTrue(result.getProduct().contains(product));
        assertTrue(result.getUser().contains(buyer));

        verify(productRepo, times(1)).findById(productId);
        verify(userRepo, times(1)).findByUserEmail("user@example.com");
        verify(cartRepo, times(1)).findByUser(buyer);
        verify(cartRepo, times(1)).save(any(Cart.class));
    }

    @Test
    public void testAddToCartProductAlreadyInCart() {
        Integer productId = 1;
        Product product = new Product();
        product.setProductId(productId);

        User buyer = new User();
        buyer.setUserEmail("user@example.com");

        Cart existingCart = new Cart();
        existingCart.setUser(Set.of(buyer));
        existingCart.setProduct(Set.of(product));

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        when(userRepo.findByUserEmail("user@example.com")).thenReturn(Optional.of(buyer));
        when(cartRepo.findByUser(buyer)).thenReturn(Collections.singletonList(existingCart));

        ProductException thrown = assertThrows(ProductException.class, () -> {
            cartService.addToCart(productId);
        });

        assertEquals("Product already exists in cart", thrown.getMessage());
    }

    @Test
    public void testGetCartDetailsSuccess() {
        User buyer = new User();
        buyer.setUserEmail("user@example.com");

        Cart cart = new Cart();
        cart.setUser(Set.of(buyer));

        when(userRepo.findByUserEmail("user@example.com")).thenReturn(Optional.of(buyer));
        when(cartRepo.findByUser(buyer)).thenReturn(Collections.singletonList(cart));

        List<Cart> result = cartService.getCatDetails();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(cart, result.get(0));
    }
}
