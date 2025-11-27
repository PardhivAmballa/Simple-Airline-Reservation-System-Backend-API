package com.airline_reservation_system.controller;

import com.airline_reservation_system.model.User;
import com.airline_reservation_system.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    // Endpoint for user registration
    @PostMapping("/register")
    public String registerUser(@RequestBody User user) {
        userService.registerUser(user);
        return "Passenger account created successfully!";
    }

    // Endpoint for admin registration
    @PostMapping("/admin/register")
    @PreAuthorize("hasRole('ADMIN')")
    public String registerNewAdmin(@RequestBody User user) {
        userService.createAdmin(user);
        return "New Admin "+ user.getUsername() + " has been added to the system.";
    }
}
