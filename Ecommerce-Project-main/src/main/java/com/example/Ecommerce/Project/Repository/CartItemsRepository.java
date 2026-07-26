package com.example.Ecommerce.Project.Repository;

import com.example.Ecommerce.Project.Entity.Cart;
import com.example.Ecommerce.Project.Entity.CartItems;
import com.example.Ecommerce.Project.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

   // Cart findByUser(Users user);
    List<CartItems> findByCart (Cart cart);
}
