package com.example.shop4All_backend.message_flow;

import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.exceptions.ProductException;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DiscountTests {

    @Mock
    private ProductRepo productRepo;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSetDiscountSuccess() {
        Integer productId = 1;
        Product product = new Product();
        product.setProductId(productId);
        product.setProductDiscounted(20.0);
        product.setProductFromDiscounted(LocalDate.now().plusDays(1));
        product.setProductToDiscounted(LocalDate.now().plusDays(10));

        Product updatedProduct = new Product();
        updatedProduct.setProductId(productId);
        updatedProduct.setProductDiscounted(20.0);
        updatedProduct.setProductFromDiscounted(LocalDate.now().plusDays(1));
        updatedProduct.setProductToDiscounted(LocalDate.now().plusDays(10));

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));
        when(productRepo.save(any(Product.class))).thenReturn(updatedProduct);

        Product resultProduct = productService.setDiscount(product);

        assertNotNull(resultProduct, "Product should not be null");
        assertEquals(20.0, resultProduct.getProductDiscounted(), "Discount should be 20.0");
    }

    @Test
    public void testSetDiscountInvalidStartDate() {
        Integer productId = 1;
        Product product = new Product();
        product.setProductId(productId);
        product.setProductDiscounted(20.0);
        product.setProductFromDiscounted(LocalDate.now().minusDays(1));
        product.setProductToDiscounted(LocalDate.now().plusDays(10));

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));

        ProductException thrown = assertThrows(ProductException.class, () -> productService.setDiscount(product));
        assertEquals("Your start date (" + product.getProductFromDiscounted() + ") is before today (" + LocalDate.now() + ") .", thrown.getMessage());
    }

    @Test
    public void testSetDiscountStartDateEqualToEndDate() {
        Integer productId = 1;
        Product product = new Product();
        product.setProductId(productId);
        product.setProductDiscounted(20.0);
        product.setProductFromDiscounted(LocalDate.now().plusDays(1));
        product.setProductToDiscounted(LocalDate.now().plusDays(1));

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));

        ProductException thrown = assertThrows(ProductException.class, () -> productService.setDiscount(product));
        assertEquals("Start date (" + product.getProductFromDiscounted() + ") and end date (" + product.getProductToDiscounted() + ") are the same.", thrown.getMessage());
    }

    @Test
    public void testSetDiscountStartDateNotBeforeEndDate() {
        Integer productId = 1;
        Product product = new Product();
        product.setProductId(productId);
        product.setProductDiscounted(20.0);
        product.setProductFromDiscounted(LocalDate.now().plusDays(10));
        product.setProductToDiscounted(LocalDate.now().plusDays(1));

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));

        ProductException thrown = assertThrows(ProductException.class, () -> productService.setDiscount(product));
        assertEquals("Your start (" + product.getProductFromDiscounted() + ") date is not before the end date (" + product.getProductToDiscounted() + ").", thrown.getMessage());
    }

    @Test
    public void testSetDiscountInvalidDiscountPercentage() {
        Integer productId = 1;
        Product product = new Product();
        product.setProductId(productId);
        product.setProductDiscounted(-5.0);
        product.setProductFromDiscounted(LocalDate.now().plusDays(1));
        product.setProductToDiscounted(LocalDate.now().plusDays(10));

        when(productRepo.findById(productId)).thenReturn(Optional.of(product));

        ProductException thrown = assertThrows(ProductException.class, () -> productService.setDiscount(product));
        assertEquals("Your discount is negative or above 100.", thrown.getMessage());
    }

    @Test
    public void testSetDiscountProductNotFound() {
        Integer productId = 1;
        Product product = new Product();
        product.setProductId(productId);
        product.setProductDiscounted(20.0);
        product.setProductFromDiscounted(LocalDate.now().plusDays(1));
        product.setProductToDiscounted(LocalDate.now().plusDays(10));

        when(productRepo.findById(productId)).thenReturn(Optional.empty());

        ProductException thrown = assertThrows(ProductException.class, () -> productService.setDiscount(product));
        assertEquals("Product not found", thrown.getMessage());
    }
}
