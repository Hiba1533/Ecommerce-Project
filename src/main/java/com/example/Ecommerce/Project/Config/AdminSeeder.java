package com.example.Ecommerce.Project.Config;

import com.example.Ecommerce.Project.Service.UserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder implements CommandLineRunner {

    private final UserService userService;

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    @Value("${admin.phone}")
    private String adminPhone;

    public AdminSeeder(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void run(String... args) {
        userService.createAdminIfNotExists(adminUsername, adminEmail, adminPassword, adminPhone);
    }
}