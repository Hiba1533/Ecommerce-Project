package com.example.Ecommerce.Project.Repository;

import com.example.Ecommerce.Project.Entity.Role;
import com.example.Ecommerce.Project.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Long> {

     Optional<Users> findByEmail(String email);

     boolean existsByEmail(String email);

     List<Users> findByRole(Role role);
}

