package com.example.Ecommerce.Project.Repository;

import com.example.Ecommerce.Project.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<Users, Long> {

     Boolean existsByEmail(String email);
     Users findByEmail(String email);

}
