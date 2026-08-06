package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Role;
import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Service.OrdersService;
import com.example.Ecommerce.Project.Service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final OrdersService ordersService;

    public AdminController(UserService userService, OrdersService ordersService) {
        this.userService = userService;
        this.ordersService = ordersService;
    }

    @GetMapping("/users")
    public List<Users> getAllUsers() {
        return userService.getAllUsers();
    }

    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "User deleted";
    }

    @PutMapping("/users/{id}/role")
    public Users updateUserRole(@PathVariable Long id, @RequestParam Role role) {
        return userService.updateUserRole(id, role);
    }
}