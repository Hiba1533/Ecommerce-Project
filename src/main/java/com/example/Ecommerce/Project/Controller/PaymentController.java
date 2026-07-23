package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Payment;
import com.example.Ecommerce.Project.Service.PaymentService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/{orderId}")
    public Payment makePayment(@PathVariable Long orderId) {

        return paymentService.makePayment(orderId);
    }

    @GetMapping("/{id}")
    public Payment getPayment(@PathVariable Long id) {

        return paymentService.getPayment(id);
    }
}