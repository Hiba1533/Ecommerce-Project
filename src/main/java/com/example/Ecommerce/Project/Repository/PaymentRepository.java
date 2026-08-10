package com.example.Ecommerce.Project.Repository;

import com.example.Ecommerce.Project.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Payment findByOrder_Id(Long order_Id);

}