package com.example.Ecommerce.Project.Service;

import com.example.Ecommerce.Project.Entity.Role;
import com.example.Ecommerce.Project.Entity.Users;
import com.example.Ecommerce.Project.Repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

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
        newUser.setRole(Role.USER);
        newUser.setCreatedAt(LocalDateTime.now());

        return userRepository.save(newUser);

    }

    public String login(Users user){
        Users existingUser = userRepository.findByEmail(user.getEmail());


        if(existingUser==null){
            throw new RuntimeException("user not found");
        }

<<<<<<< Updated upstream
        if(!passwordEncoder.matches(user.getPassword(), existingUser.getPassword())){
            throw new RuntimeException("incorrect pass");
        }
        return jwtService.generateToken(existingUser.getEmail());
    }

=======
        return jwtService.generateToken(existingUser.getEmail(), existingUser.getRole().name());
    }

    public Users getUserByEmail(String email) {

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        userRepository.deleteById(id);
    }

    public Users updateUserRole(Long id, Role role) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(role);
        return userRepository.save(user);
    }

    public void createAdminIfNotExists(String username, String email, String password, String phone) {
        if (userRepository.existsByEmail(email)) {
            return;
        }
        Users admin = new Users();
        admin.setUsername(username);
        admin.setEmail(email);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setPhone(phone);
        admin.setRole(Role.ADMIN);
        admin.setCreatedAt(LocalDateTime.now());
        userRepository.save(admin);
    }
>>>>>>> Stashed changes
}