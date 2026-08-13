package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Orders;
import com.example.Ecommerce.Project.Entity.Product;
import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Service.CategoryService;
import com.example.Ecommerce.Project.Service.OrdersService;
import com.example.Ecommerce.Project.Service.ProductService;
import com.example.Ecommerce.Project.Service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/merchant")

public class MerchantController {

    private final UserService userService;
    private final OrdersService ordersService;

    public MerchantController(UserService userService, OrdersService ordersService) {
        this.userService = userService;
        this.ordersService = ordersService;
    }

    @PostMapping("/register")
    public Users registerMerchant(@RequestBody Users user){
        return userService.registerMerchant(user);
    }

    @GetMapping("/orders")
    public List<Orders> getMerchantOrders(
            Authentication authentication) {

        return ordersService.getMerchantOrders(authentication);
    }

    @PutMapping("/orders/{id}/refund")
    public Orders refundOrder(
            @PathVariable Long id,
            Authentication authentication) {

        return ordersService.refundOrderByMerchant(id, authentication);
    }

}