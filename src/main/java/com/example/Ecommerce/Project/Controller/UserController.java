package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public Users register(@RequestBody Users user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody Users user,
                        HttpServletResponse response) {
        String token = userService.login(user);
        Users loggedInUser = userService.getUserByEmail(user.getEmail());
        response.setHeader("Authorization", "Bearer " + token);
        response.setHeader("User-Id", loggedInUser.getId().toString());

        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60); // 1 day

        response.addCookie(cookie);

        return "Login success";
    }
}