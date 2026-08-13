package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.*;
import com.example.Ecommerce.Project.Repository.OrderRepository;
import com.example.Ecommerce.Project.Repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.core.Authentication;

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
        // Create new order
        Orders newOrder = new Orders();
        newOrder.setUser(user);
        newOrder.setName(order.getName());
        newOrder.setPhone(order.getPhone());
        newOrder.setAddress(order.getAddress());
        // Get user's cart
        Cart cart = cartService.getOrCreateCart(user);
        // Total amount
        BigDecimal totalAmount = BigDecimal.ZERO;
        // Store order items
        List<OrderItems> orderItems = new ArrayList<>();
        // Loop through cart
        for (CartItems item : cart.getCartItems()) {
            // Product price
            BigDecimal productPrice = item.getProduct().getPrice();
            // Quantity
            BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());
            // Calculate total
            totalAmount = totalAmount.add(
                    productPrice.multiply(quantity)
            );
            // Create OrderItem
            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(newOrder);
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItems.add(orderItem);
        }
        // Set order information
        newOrder.setAmount(totalAmount);
        newOrder.setStatus(OrderStatus.pending);
        newOrder.setOrderDate(LocalDateTime.now());
        // Attach order items to order
        newOrder.setOrderItems(orderItems);
        // Save order
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

    public Orders refundOrderByMerchant(Long orderId, Authentication authentication) {

        // Find logged-in merchant
        Users merchant = userRepository.findByEmail(
                authentication.getName()
        ).orElseThrow(() ->
                new RuntimeException("Merchant not found")
        );

        if (merchant.getRole() != Role.MERCHANT) {
            throw new RuntimeException(
                    "Only merchants can refund orders"
            );
        }

        Orders order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        boolean ownsProductInOrder = order.getOrderItems().stream().anyMatch(
                item -> item.getProduct().getMerchant() != null &&
                        item.getProduct().getMerchant().getId().equals(merchant.getId())
        );

        if (!ownsProductInOrder) {
            throw new RuntimeException(
                    "You can only refund orders that include your own products"
            );
        }

        return refundOrder(orderId);
    }

    public List<Orders> getMerchantOrders(Authentication authentication) {

        // Find logged-in merchant
        Users merchant = userRepository.findByEmail(
                authentication.getName()
        ).orElseThrow(() ->
                new RuntimeException("Merchant not found")
        );

        // Check role
        if (merchant.getRole() != Role.MERCHANT) {
            throw new RuntimeException(
                    "Only merchants can view orders"
            );
        }

        // Get all orders
        List<Orders> allOrders = orderRepository.findAll();

        // Store merchant's orders
        List<Orders> merchantOrders = new ArrayList<>();

        // Check every order
        for (Orders order : allOrders) {

            // Check every item in the order
            for (OrderItems item : order.getOrderItems()) {

                // Get product's merchant
                Users productMerchant =
                        item.getProduct().getMerchant();

                // Check if product belongs to logged-in merchant
                if (productMerchant != null &&
                        productMerchant.getId()
                                .equals(merchant.getId())) {

                    merchantOrders.add(order);

                    // Don't add same order twice
                    break;
                }
            }
        }

        return merchantOrders;
    }
}