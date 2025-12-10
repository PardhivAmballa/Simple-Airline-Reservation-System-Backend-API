package com.airline_reservation_system.service;

import com.airline_reservation_system.model.User;
import com.airline_reservation_system.util.LogUtil;
import com.airline_reservation_system.persistence.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {

    // Repository for CRUD operations on User entities
    @Autowired
    private UserRepository userRepository;

    // Password encoder for hashing passwords
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Initialize a built-in admin user when the application starts (if not already present)
    @PostConstruct
    public void initRootAdmin() {
        if(!userRepository.existsByUsername("Pardhiv")){
            User rootAdmin = new User("Pardhiv", passwordEncoder.encode("kali123"), "ROLE_ADMIN");
            userRepository.save(rootAdmin);
            LogUtil.system("Root admin initialized at startup");
            System.out.println("System Initialized: Inbuilt Admin User Created");
        }
    }

    // Register a new user with ROLE_USER
    public User registerUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())){
            LogUtil.error("Registration failed: Username already exists -> " + user.getUsername());
            throw new RuntimeException("Username already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);
        LogUtil.activity("New user registered: " + user.getUsername());
        return user;
    }

    // Create a new admin user with ROLE_ADMIN
    public User createAdmin(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_ADMIN");
        userRepository.save(user);
        LogUtil.system("ADMIN created new admin user " + user.getUsername());
        return user;
    }
}