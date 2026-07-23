package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Orders;
import com.example.Ecommerce.Project.Service.OrdersService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrdersController {

    private final OrdersService ordersService;

    public OrdersController(OrdersService ordersService) {
        this.ordersService = ordersService;
    }

    @PostMapping("/{userId}")
    public Orders placeOrder(@PathVariable Long userId,
                             @RequestBody Orders order) {

        return ordersService.placeOrder(userId, order);
    }

    @GetMapping("/{userId}")
    public List<Orders> getOrders(@PathVariable Long userId) {

        return ordersService.getOrders(userId);
    }

    @GetMapping("/details/{id}")
    public Orders getOrderById(@PathVariable Long id) {

        return ordersService.getOrderById(id);
    }

}