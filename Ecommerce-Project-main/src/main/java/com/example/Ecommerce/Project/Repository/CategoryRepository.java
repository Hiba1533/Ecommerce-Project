package com.example.Ecommerce.Project.Repository;

import com.example.Ecommerce.Project.Entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
