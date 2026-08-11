package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Service.CategoryService;
import com.example.Ecommerce.Project.Service.ProductService;
import com.example.Ecommerce.Project.Service.UserService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/merchant")

public class MerchantController {

    private final UserService userService;

    public MerchantController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Users registerMerchant(@RequestBody Users user){
        return userService.registerMerchant(user);
    }
}
