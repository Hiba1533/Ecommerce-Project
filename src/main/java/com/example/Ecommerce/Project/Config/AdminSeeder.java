package com.example.Ecommerce.Project.Config;

import com.example.Ecommerce.Project.Service.UserService;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class AdminSeeder {


    private final UserService userService;


    //@value--> get value from application.properties
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


    //after you create adminseeder and everything is set up automatically run createAdmin()
    @PostConstruct
    public void createAdmin() {
        userService.createAdminIfNotExists(
                adminUsername,
                adminEmail,
                adminPassword,
                adminPhone
        );
    }
}