package com.example.shop4All_backend.message_flow;

import com.example.shop4All_backend.configurations.JwtRequestFilter;
import com.example.shop4All_backend.entities.*;
import com.example.shop4All_backend.exceptions.ProductException;
import com.example.shop4All_backend.exceptions.UserException;
import com.example.shop4All_backend.repositories.CartRepo;
import com.example.shop4All_backend.repositories.OrderDetailsRepo;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.repositories.UserRepo;
import com.example.shop4All_backend.services.EmailService;
import com.example.shop4All_backend.services.OrderDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class OrderDetailsTests {

    @InjectMocks
    private OrderDetailsService orderDetailsService;

    @Mock
    private OrderDetailsRepo orderDetailsRepo;

    @Mock
    private ProductRepo productRepo;

    @Mock
    private UserRepo userRepo;

    @Mock
    private CartRepo cartRepo;

    @Mock
    private EmailService emailService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        JwtRequestFilter.CURRENT_USER = "user@example.com";
    }

    @Test
    public void testPlaceOrderSuccess() {
        OrderInput orderInput = new OrderInput();
        orderInput.setFullName("John Doe");
        orderInput.setFullAddress("123 Street, City");
        orderInput.setContactNumber("1234567890");
        orderInput.setProducts(Collections.singletonList(new OrderProductQuantity(1, 2)));

        Product product = new Product();
        product.setProductId(1);
        product.setProductName("Product1");
        product.setProductQuantity(10);
        product.setProductPrice(100.0);
        product.setProductDiscounted(10.0);
        product.setCompanySeller("Company1");

        User user = new User();
        user.setUserEmail("user@example.com");
        user.setUserCompanyName("Company1");

        OrderDetails orderDetails = OrderDetails.builder()
                .orderFullName("John Doe")
                .orderFullAddress("123 Street, City")
                .orderContactNumber("1234567890")
                .orderStatus("Placed")
                .orderAmount(180.0)
                .orderPrice(90.0)
                .orderQuantity(2)
                .orderDate(LocalDate.now())
                .product(product)
                .user(user)
                .build();

        when(productRepo.findById(1)).thenReturn(Optional.of(product));
        when(userRepo.findByUserEmail("user@example.com")).thenReturn(Optional.of(user));
        when(orderDetailsRepo.saveAll(anyList())).thenReturn(Collections.singletonList(orderDetails));
        when(cartRepo.findByUser(user)).thenReturn(Collections.emptyList());

        List<OrderDetails> result = orderDetailsService.placeOrder(true, orderInput);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(orderDetails.getOrderContactNumber(), result.get(0).getOrderContactNumber());
        assertEquals(8, product.getProductQuantity());
    }

    @Test
    public void testPlaceOrderInsufficientStock() {
        OrderInput orderInput = new OrderInput();
        orderInput.setFullName("John Doe");
        orderInput.setFullAddress("123 Street, City");
        orderInput.setContactNumber("1234567890");
        orderInput.setProducts(Collections.singletonList(new OrderProductQuantity(1, 15)));

        User user = new User();
        user.setUserEmail("user@example.com");
        user.setUserCompanyName("Company1");

        Product product = new Product();
        product.setProductId(1);
        product.setProductQuantity(10);

        when(productRepo.findById(1)).thenReturn(Optional.of(product));
        when(userRepo.findByUserEmail("user@example.com")).thenReturn(Optional.of(user));

        ProductException thrown = assertThrows(ProductException.class, () -> {
            orderDetailsService.placeOrder(true, orderInput);
        });

        assertEquals("Not enough stock for product null", thrown.getMessage());
    }

    @Test
    public void testPlaceOrderValidationFailure() {
        OrderInput orderInput = new OrderInput();
        orderInput.setFullName(null);
        orderInput.setFullAddress(null);
        orderInput.setContactNumber(null);

        User user = new User();
        user.setUserEmail("user@example.com");

        UserException thrown = assertThrows(UserException.class, () -> {
            orderDetailsService.placeOrder(true, orderInput);
        });

        assertEquals("Fill all the required fields", thrown.getMessage());
    }

    @Test
    public void testPlaceOrderWithCart() {
        OrderInput orderInput = new OrderInput();
        orderInput.setFullName("John Doe");
        orderInput.setFullAddress("123 Street, City");
        orderInput.setContactNumber("1234567890");
        orderInput.setProducts(Collections.singletonList(new OrderProductQuantity(1, 2)));

        Product product = new Product();
        product.setProductId(1);
        product.setProductQuantity(10);
        product.setProductPrice(100.0);
        product.setProductDiscounted(10.0);
        product.setCompanySeller("Company1");

        User user = new User();
        user.setUserEmail("user@example.com");
        user.setUserCompanyName("Company1");

        Cart cart = new Cart();
        cart.setProduct(Collections.singleton(product));
        cart.setUser(Collections.singleton(user));

        when(productRepo.findById(1)).thenReturn(Optional.of(product));
        when(userRepo.findByUserEmail("user@example.com")).thenReturn(Optional.of(user));
        when(cartRepo.findByUser(user)).thenReturn(Collections.singletonList(cart));
        when(orderDetailsRepo.saveAll(anyList())).thenReturn(Collections.emptyList());

        List<OrderDetails> result = orderDetailsService.placeOrder(false, orderInput);

        assertNotNull(result);
        assertEquals(8, product.getProductQuantity());

       }
}
