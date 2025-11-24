package com.airline_reservation_system.persistence;

import com.airline_reservation_system.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList; // You need this import
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {
    private static final String FILE_PATH = "users.json";

    @Autowired
    private JsonFileUtil jsonFileUtil;

    public List<User> findAll() {
        return jsonFileUtil.readData(FILE_PATH, new TypeReference<List<User>>() {});
    }

    public Optional<User> findByUsername(String username) {
        return findAll().stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst();
    }

    // 💡 FIX: Wrapped findAll() in 'new ArrayList<>()' to make it mutable
    public void save(User user) {
        // Create a new mutable list from the data read from the file
        List<User> users = new ArrayList<>(findAll());

        // Remove existing user if updating (ensures uniqueness)
        users.removeIf(u -> u.getUsername().equals(user.getUsername()));

        // Add the new/updated user
        users.add(user);

        jsonFileUtil.writeData(FILE_PATH, users);
    }

    // Helper method used in UserService
    public boolean existsByUsername(String username) {
        return findByUsername(username).isPresent();
    }
}