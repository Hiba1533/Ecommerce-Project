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

    private final OrderRepository ordersRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final OrderRepository orderRepository;
    private final PaymentService paymentService;

    public OrdersService(OrderRepository ordersRepository,
                         UserRepository userRepository, CartService cartService, OrderRepository orderRepository, PaymentService paymentService) {
        this.ordersRepository = ordersRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.orderRepository = orderRepository;
        this.paymentService = paymentService;
    }

    public Orders placeOrder(Long userId, Orders order) {

        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Orders newOrder = new Orders();
        newOrder.setUser(user);
        newOrder.setName(order.getName());
        newOrder.setPhone(order.getPhone());
        newOrder.setAddress(order.getAddress());
        Cart cart = cartService.getOrCreateCart(user);
        BigDecimal totalAmount = BigDecimal.ZERO;

        for (CartItems item : cart.getCartItems()) {
            BigDecimal productPrice = item.getProduct().getPrice();
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            totalAmount = totalAmount.add(productPrice.multiply(quantity));

        }
        newOrder.setAmount(totalAmount);
        newOrder.setStatus(OrderStatus.pending);
        newOrder.setOrderDate(LocalDateTime.now());
        Orders savedOrder = ordersRepository.save(newOrder);
        cartService.clearCart(cart);
        return savedOrder;
    }

    public Orders payOrder(Long orderId){
        Orders orders = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        orders.setPaymentStatus(PaymentStatus.paid);
        return orderRepository.save(orders);
    }


    public List<Orders> getOrders(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return ordersRepository.findByUser(user);
    }

    public Orders getOrderById(Long id) {
        return ordersRepository.findById(id).orElseThrow(() -> new RuntimeException("Order not found"));
    }

    public List<Orders> getOrdersByUser(Long userId) {

        if (!userRepository.existsById(userId)) {
            throw new RuntimeException("User not found");
        }

        return orderRepository.findByUserId(userId);
    }

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    public Orders requestRefund(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getPaymentStatus() != PaymentStatus.paid) {
            throw new RuntimeException("Refund can only be requested for paid orders");
        }

        order.setPaymentStatus(PaymentStatus.refund_requested);
        return orderRepository.save(order);
    }

    public Orders refundOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        if (order.getPaymentStatus() != PaymentStatus.refund_requested) {
            throw new RuntimeException("Refund was not requested for this order");
        }

        order.setPaymentStatus(PaymentStatus.refunded);
        Orders updatedOrder = orderRepository.save(order);

        paymentService.markPaymentAsRefunded(orderId);

        return updatedOrder;
    }

}