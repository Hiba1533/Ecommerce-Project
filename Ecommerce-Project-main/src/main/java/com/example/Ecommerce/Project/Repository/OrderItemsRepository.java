package com.example.Ecommerce.Project.Repository;

import com.example.Ecommerce.Project.Entity.OrderItems;
import com.example.Ecommerce.Project.Entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemsRepository extends JpaRepository<OrderItems, Long> {
        List<OrderItems> findByOrder(Orders order);
    }
