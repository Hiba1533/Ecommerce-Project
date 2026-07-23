package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.CartItems;
import com.example.Ecommerce.Project.Service.CartItemsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart/items")
public class CartItemController {

    private final CartItemsService cartItemsService;

    public CartItemController(CartItemsService cartItemsService) {
        this.cartItemsService = cartItemsService;
    }

    @PostMapping
    public CartItems addToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam Integer quantity){
        return cartItemsService.addToCart(userId, productId, quantity);
    }

    @GetMapping("/{cartItemId}")
    public CartItems updateQuantity(@PathVariable Long cartItemId, @RequestBody Integer quantity){
        return cartItemsService.updateQuantity(cartItemId, quantity);
    }


    @DeleteMapping("/{cartItemId}")
    public String deleteCartItem(@PathVariable Long cartItemId) {
        cartItemsService.deleteCartItem(cartItemId);
        return "Cart item deleted";
    }
}
