package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.*;
import com.example.Ecommerce.Project.Repository.OrderRepository;
import com.example.Ecommerce.Project.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdersService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final PaymentService paymentService;

    public OrdersService(OrderRepository orderRepository, UserRepository userRepository, CartService cartService,
                         PaymentService paymentService) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.paymentService = paymentService;
    }

    public Orders placeOrder(Long userId, Orders order) {
        // Find user
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Orders newOrder = new Orders();
        // Copy information
        newOrder.setUser(user);
        newOrder.setName(order.getName());
        newOrder.setPhone(order.getPhone());
        newOrder.setAddress(order.getAddress());
        // Get user cart
        Cart cart = cartService.getOrCreateCart(user);
        // Calculate total
        BigDecimal totalAmount = BigDecimal.ZERO;
        // Loop through cart for products
        for (CartItems item : cart.getCartItems()) {
            // Get product price
            BigDecimal productPrice = item.getProduct().getPrice();
            // Get quantity
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            totalAmount = totalAmount.add(
                    productPrice.multiply(quantity)
            );
        }

        newOrder.setAmount(totalAmount);
        // When order is created, its status is pending
        newOrder.setStatus(OrderStatus.pending);
        newOrder.setOrderDate(LocalDateTime.now());
        Orders savedOrder = orderRepository.save(newOrder);
        // Clear cart
        cartService.clearCart(cart);
        return savedOrder;
    }

    public Orders payOrder(Long orderId) {

        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orders.setPaymentStatus(PaymentStatus.paid);
        return orderRepository.save(orders);
    }

    public Orders getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Orders> getOrdersByUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }
        return orderRepository.findByUserId(userId);
    }

    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    public Orders requestRefund(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        if (order.getPaymentStatus() != PaymentStatus.paid) {
            throw new RuntimeException(
                    "Refund can only be requested for paid orders"
            );
        }
        order.setPaymentStatus(PaymentStatus.refund_requested);
        return orderRepository.save(order);
    }

    public Orders refundOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        // Check for refund request
        if (order.getPaymentStatus() != PaymentStatus.refund_requested) {
            throw new RuntimeException(
                    "Refund was not requested for this order"
            );
        }

        order.setPaymentStatus(PaymentStatus.refunded);
        Orders updatedOrder = orderRepository.save(order);
        // Update payment status for that order
        paymentService.markPaymentAsRefunded(orderId);
        return updatedOrder;
    }
}