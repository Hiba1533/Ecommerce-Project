package com.example.Ecommerce.Project.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder

public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, scale = 2)
    private BigDecimal amount;

    private LocalDateTime paidAt;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    @OneToOne
    @JoinColumn(name="order_id", nullable = false)
    private Orders order;
}
