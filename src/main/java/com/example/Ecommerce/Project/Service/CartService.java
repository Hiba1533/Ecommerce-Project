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

    public Cart getOrCreateCart(Users user) {

        return cartRepository.findByUser(user)
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    cart.setCreatedAt(LocalDateTime.now());

                    return cartRepository.save(cart);
                });
    }

    public BigDecimal getCartTotal(Cart cart) {
        BigDecimal total = BigDecimal.ZERO;
        if (cart.getCartItems() != null) {
            for (CartItems item : cart.getCartItems()) {
                BigDecimal itemTotal =
                        item.getProduct()
                                .getPrice()
                                .multiply(BigDecimal.valueOf(item.getQuantity()));

                total = total.add(itemTotal);
            }
        }
        return total;
    }
    public void clearCart(Cart cart) {
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}