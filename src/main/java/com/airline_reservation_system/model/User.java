package com.airline_reservation_system.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String username;
    private String password; //This will be encrypted (BCrypt)
    private String role; // "admin" or "passenger"
}
