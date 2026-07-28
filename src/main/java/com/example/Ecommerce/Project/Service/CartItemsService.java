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

@Service
public class CartItemsService {

    private final CartItemsRepository cartItemsRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartService cartService;

    public CartItemsService(CartItemsRepository cartItemsRepository, CartRepository cartRepository, ProductRepository productRepository,
                            UserRepository userRepository, CartService cartService) {

        this.cartItemsRepository = cartItemsRepository;
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
    }

    public CartItems addToCart(Long userId, Long productId, Integer quantity) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartService.getOrCreateCart(user);
        Product product = productRepository.findById(productId).orElseThrow(() -> new RuntimeException("Product not found"));

        CartItems item = new CartItems();
        item.setCart(cart);
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setAddAt(LocalDateTime.now());

        return cartItemsRepository.save(item);
    }

    public List<CartItems> getCartItems(Long userId) {

        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartService.getOrCreateCart(user);
        return cartItemsRepository.findByCart(cart);
    }

    public CartItems updateQuantity(Long cartItemId, Integer quantity) {
        CartItems item = cartItemsRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("Cart item not found"));
        item.setQuantity(quantity);

        return cartItemsRepository.save(item);
    }

    public void deleteCartItem(Long cartItemId) {
        CartItems item = cartItemsRepository.findById(cartItemId).orElseThrow(() -> new RuntimeException("Cart item not found"));

        cartItemsRepository.delete(item);
    }
}