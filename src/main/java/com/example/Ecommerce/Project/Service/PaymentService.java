package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.Orders;
import com.example.Ecommerce.Project.Entity.Payment;
import com.example.Ecommerce.Project.Repository.OrderRepository;
import com.example.Ecommerce.Project.Repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository ordersRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderRepository orderRepository){
        this.paymentRepository = paymentRepository;
        this.ordersRepository=orderRepository;
    }

    public Payment makePayment(Long orderId){
        Orders order=ordersRepository.findById(orderId).orElseThrow(()->new RuntimeException("order not found"));
        Payment payment=new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getAmount());
        payment.setStatus("placed");
        payment.setPaidAt(LocalDateTime.now());
        return paymentRepository.save(payment);
    }

    public Payment getPayment(Long id) {
        return paymentRepository.findById(id).orElseThrow(()->new RuntimeException("payment not found"));
    }

    public void markPaymentAsRefunded(Long orderId) {
        Payment payment = paymentRepository.findByOrder_Id(orderId);

        if (payment != null) {
            payment.setStatus("refunded");
            paymentRepository.save(payment);
        }
    }
}