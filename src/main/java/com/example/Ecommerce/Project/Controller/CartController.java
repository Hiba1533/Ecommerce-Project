package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Cart;
import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Repository.UserRepository;
import com.example.Ecommerce.Project.Service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }


    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Long userId){
        Users user=userRepository.findById(userId).orElseThrow(()-> new RuntimeException("no user found"));
        return cartService.getOrCreateCart(user);
    }

    @DeleteMapping("/{userId}")
    public String clearCart(@PathVariable Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartService.getOrCreateCart(user);
        cartService.clearCart(cart);
        return "Cart cleared";
    }


}
