package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.Orders;
import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Repository.OrderRepository;
import com.example.Ecommerce.Project.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdersService {

    private final OrderRepository ordersRepository;
    private final UserRepository userRepository;

    public OrdersService(OrderRepository ordersRepository,
                         UserRepository userRepository) {
        this.ordersRepository = ordersRepository;
        this.userRepository = userRepository;
    }

    public Orders placeOrder(Long userId, Orders order) {

        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Orders newOrder = new Orders();

        newOrder.setUser(user);
        newOrder.setName(order.getName());
        newOrder.setPhone(order.getPhone());
        newOrder.setAddress(order.getAddress());
        newOrder.setAmount(order.getAmount());
        newOrder.setOrderDate(LocalDateTime.now());
        return ordersRepository.save(newOrder);
    }

    public List<Orders> getOrders(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return ordersRepository.findByUser(user);
    }

    public Orders getOrderById(Long id) {
        return ordersRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }
}