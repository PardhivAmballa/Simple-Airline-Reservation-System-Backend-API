package com.airline_reservation_system.persistence;

import com.airline_reservation_system.model.User;
import com.airline_reservation_system.util.LogUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private static final String FILE_PATH = "users.json";

    @Autowired
    private JsonFileUtil jsonFileUtil;

    // Retrieve all users
    public List<User> findAll() {
        LogUtil.system("Reading all users");
        return jsonFileUtil.readData(FILE_PATH, new TypeReference<List<User>>() {});
    }

    // Method to find a user by username
    public Optional<User> findByUsername(String username) {
        LogUtil.system("Finding user: " + username);
        return findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    // Save or update a user
    public void save(User user) {
        List<User> users = new ArrayList<>(findAll());
        // Remove existing user if updating (ensures uniqueness)
        users.removeIf(u -> u.getUsername().equals(user.getUsername()));
        // Add the new/updated user
        LogUtil.activity("Saving user: " + user.getUsername());
        users.add(user);
        jsonFileUtil.writeData(FILE_PATH, users);
    }

    // Helper method used in UserService
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }
}