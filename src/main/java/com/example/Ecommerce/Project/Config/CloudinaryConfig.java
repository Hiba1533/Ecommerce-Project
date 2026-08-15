package com.example.Ecommerce.Project.Config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {

        return new Cloudinary(
                "cloudinary://331686241276847:MTSbBBXcLpM0myBQVJTj1mDphTo@t56uxyvf"
        );
    }
}
