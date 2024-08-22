package com.example.shop4All_backend.services;

import com.example.shop4All_backend.configurations.JwtRequestFilter;
import com.example.shop4All_backend.entities.Cart;
import com.example.shop4All_backend.exceptions.ProductException;
import com.example.shop4All_backend.repositories.CartRepo;
import com.example.shop4All_backend.repositories.ProductRepo;
import com.example.shop4All_backend.entities.Product;
import com.example.shop4All_backend.entities.User;
import com.example.shop4All_backend.repositories.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.HashSet;
import java.util.Set;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepo cartRepo;
    private final ProductRepo productRepo;
    private final UserRepo userRepo;

    //add a product to buyer's cart
    public Cart addToCart(Integer productId) {
        Product product = productRepo.findById(productId).get();
        String buyerName = JwtRequestFilter.CURRENT_USER;
        User buyer = null;
        if (buyerName != null)
            buyer = userRepo.findByUserEmail(buyerName).get();

        List<Cart> cartList = cartRepo.findByUser(buyer);
        List<Cart> filteredList = cartList.stream()
                .filter(x -> x.getProduct().stream()
                        .anyMatch(p -> p.getProductId() == productId))
                .toList();

        if (filteredList.size() > 0)
            throw new ProductException("Product already exists in cart");

        if (product != null && buyer != null) {
            Cart cart = new Cart();
            Set<User> buyers = new HashSet<>();
            buyers.add(buyer);
            Set<Product> products = new HashSet<>();
            products.add(product);
            cart.setUser(buyers);
            cart.setProduct(products);
            return cartRepo.save(cart);
        }
        throw new ProductException("Error while adding cart to the cart");
    }

    //get buyer's cart
    public List<Cart> getCatDetails() {
        String buyerEmail = JwtRequestFilter.CURRENT_USER;
        User buyer = userRepo.findByUserEmail(buyerEmail).get();
        return cartRepo.findByUser(buyer);
    }

    //delete a product from buyer's cart
    public Cart deleteCartItem(Integer cartId) {
        Cart cart = cartRepo.findById(cartId).get();
        System.out.println(cart);
        System.out.println(cart.getProduct());
        cartRepo.deleteById(cartId);
        return cart;
    }
}
