package com.example.Ecommerce.Project.Repository;

import com.example.Ecommerce.Project.Entity.Orders;
import com.example.Ecommerce.Project.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByUser(Users user);
}