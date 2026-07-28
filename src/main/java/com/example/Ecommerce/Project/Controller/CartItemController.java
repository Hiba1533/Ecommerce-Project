package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.CartItems;
import com.example.Ecommerce.Project.Service.CartItemsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart/items")
public class CartItemController {

    private final CartItemsService cartItemsService;

    public CartItemController(CartItemsService cartItemsService) {
        this.cartItemsService = cartItemsService;
    }

    @PostMapping
    public CartItems addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam Integer quantity) {
        return cartItemsService.addToCart(userId, productId, quantity);
    }

    @GetMapping("/{userId}")
    public List<CartItems> getCartItems(@PathVariable Long userId) {
        return cartItemsService.getCartItems(userId);
    }
    @PutMapping("/{cartItemId}")
    public CartItems updateQuantity(@PathVariable Long cartItemId, @RequestParam Integer quantity) {
        return cartItemsService.updateQuantity(cartItemId, quantity);
    }

    @DeleteMapping("/{cartItemId}")
    public String deleteCartItem(@PathVariable Long cartItemId) {
        cartItemsService.deleteCartItem(cartItemId);
        return "Cart item deleted successfully";
    }
}