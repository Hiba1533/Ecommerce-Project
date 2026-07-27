package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.Cart;
import com.example.Ecommerce.Project.Entity.CartItems;
import com.example.Ecommerce.Project.Entity.Product;
import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Repository.CartItemsRepository;
import com.example.Ecommerce.Project.Repository.CartRepository;
import com.example.Ecommerce.Project.Repository.ProductRepository;
import com.example.Ecommerce.Project.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CartItemsService {
    private final CartItemsRepository cartItemsRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartItemsService(CartItemsRepository cartItemsRepository, CartRepository cartRepository,
                            ProductRepository productRepository,
                            UserRepository userRepository) {

        this.cartItemsRepository = cartItemsRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public CartItems addToCart(Long userId, Long productId, Integer quantity) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Optional<Cart> optionalCart = cartRepository.findByUser(user);
        Cart cart;

        if (optionalCart.isPresent()) {
            cart = optionalCart.get();
        } else {
            cart = new Cart();
            cart.setUser(user);
            cart.setCreatedAt(LocalDateTime.now());
            cart = cartRepository.save(cart);
        }

        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        CartItems cartItem = new CartItems();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setQuantity(quantity);
        cartItem.setAddAt(LocalDateTime.now());
        return cartItemsRepository.save(cartItem);
    }

    public List<CartItems> getCartItems(Long userId) {

        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart=cartRepository.findByUser(user).orElseThrow(()->new RuntimeException("user nto found"));
        return cartItemsRepository.findByCart(cart);
    }

    public CartItems updateQuantity(Long id, Integer quantity){
        CartItems cartItems=cartItemsRepository.findById(id).orElseThrow(()->new RuntimeException("items not found"));
        cartItems.setQuantity(quantity);
        return cartItemsRepository.save(cartItems);
    }

    public void deleteCartItem(Long id) {
        CartItems cartItem = cartItemsRepository.findById(id).orElseThrow(() -> new RuntimeException("items not found"));
        cartItemsRepository.delete(cartItem);
    }



}
