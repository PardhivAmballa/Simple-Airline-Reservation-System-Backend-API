package com.airline_reservation_system.persistence;

import com.airline_reservation_system.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    // Used in CustomUserDetailsService and UserService
    Optional<User> findByUsername(String username);

    // Used in UserService to check for duplicate usernames
    boolean existsByUsername(String username);
}