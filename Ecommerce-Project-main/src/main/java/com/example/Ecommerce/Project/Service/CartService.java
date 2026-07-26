package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.Cart;
import com.example.Ecommerce.Project.Entity.CartItems;
import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Repository.CartRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CartService {

    private final CartRepository cartRepository;

    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    // Get cart of a user
    public Cart getCartByUser(Users user) {
        return cartRepository.findByUser(user)
                .orElse(null);
    }

    // Create cart if it doesn't exist
    public Cart createCart(Users user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    // Get existing cart or create a new one
    public Cart getOrCreateCart(Users user) {
        Cart cart = getCartByUser(user);
        if (cart == null) {
            cart = createCart(user);
        }
        return cart;
    }

    public BigDecimal getCartTotal(Cart cart) {

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItems item : cart.getCartItems()) {

            BigDecimal productPrice = item.getProduct().getPrice();

            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());

            totalAmount = totalAmount.add(productPrice.multiply(quantity));

        }

        return totalAmount;
    }

    // Remove all items from cart
    public void clearCart(Cart cart) {
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}
