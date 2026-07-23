package com.example.Ecommerce.Project.Repository;

import com.example.Ecommerce.Project.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
