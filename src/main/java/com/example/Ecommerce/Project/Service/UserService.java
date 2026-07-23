package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService){
        this.userRepository=userRepository;
        this.passwordEncoder=passwordEncoder;
        this.jwtService=jwtService;
    }

    public Users register(Users user){

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("user already exist");
        }

        Users newUser=new Users();
        newUser.setUsername(user.getUsername());
        newUser.setEmail(user.getEmail());
        newUser.setPassword(passwordEncoder.encode(user.getPassword()));
        newUser.setPhone(user.getPhone());
        newUser.setCreatedAt(LocalDateTime.now());

        return userRepository.save(newUser);

    }

    public String login(Users user){
        Users existingUser = userRepository.findByEmail(user.getEmail());


        if(existingUser==null){
            throw new RuntimeException("user not found");
        }

        if(!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())){
            throw new RuntimeException("incorrect pass");
        }
        return jwtService.generateToken(existingUser.getEmail());
    }

}