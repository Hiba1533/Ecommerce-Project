package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")

public class UserController {
     private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

     @PostMapping("/register")
          public Users register(@RequestBody Users user){
               return userService.register(user);
          }

    @PostMapping("/login")
    public String login(@RequestBody Users user,
                        HttpServletResponse response) {

        String token = userService.login(user);
        response.setHeader("Authorization", "Bearer " +token);

        return "Login success";
    }

}


