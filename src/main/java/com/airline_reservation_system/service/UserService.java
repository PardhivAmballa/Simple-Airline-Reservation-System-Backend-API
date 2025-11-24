package com.airline_reservation_system.service;

import com.airline_reservation_system.model.User;
import com.airline_reservation_system.persistence.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initRootAdmin() {
        if(!userRepository.existsByUsername("Pardhiv")){
            User rootAdmin = new User("Pardhiv", passwordEncoder.encode("kali123"), "ROLE_ADMIN");
            userRepository.save(rootAdmin);
            System.out.println("System Initialized: Inbuilt Admin User Created");
        }
    }

    public User registerUser(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        // 💡 FIX: Changed ROLE_PASSENGER to ROLE_USER to match security configuration
        user.setRole("ROLE_USER");
        userRepository.save(user);
        return user;
    }

    public User createAdmin(User user) {
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("ROLE_ADMIN");
        userRepository.save(user);
        return user;
    }
}