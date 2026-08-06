package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Orders;
import com.example.Ecommerce.Project.Service.OrdersService;
import org.springframework.http.ResponseEntity;
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

    @PutMapping("/{id}/pay")
    public ResponseEntity<?> payOrder(@PathVariable Long id){
        Orders orders = ordersService.payOrder(id);
        return ResponseEntity.ok(orders);
    }

    @PutMapping("/{id}/request-refund")
    public ResponseEntity<?> requestRefund(@PathVariable Long id) {
        Orders orders = ordersService.requestRefund(id);
        return ResponseEntity.ok(orders);
    }

}