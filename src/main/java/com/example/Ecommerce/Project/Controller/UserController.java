package com.example.Ecommerce.Project.Controller;

import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
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

        Users loggedInUser =
                userService.getUserByEmail(user.getEmail());

        response.setHeader(
                "Authorization",
                "Bearer " + token
        );

        response.setHeader(
                "User-Id",
                loggedInUser.getId().toString()
        );

        Cookie cookie = new Cookie("jwt", token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(24 * 60 * 60);

        response.addCookie(cookie);

        return "Login success";
    }

    @GetMapping("/me")
    public Users getCurrentUser(Authentication authentication) {

        if (authentication == null) {
            throw new RuntimeException("User not logged in");
        }

        return userService.getUserByEmail(
                authentication.getName()
        );
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return "Logout successful";
    }
}