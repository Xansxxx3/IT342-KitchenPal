package com.g1appdev.mealplanner.repository;

import com.g1appdev.mealplanner.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> { // Change from Integer to Long

    // Find a user by their ID (Now using long)
    Optional<UserEntity> findByUserId(long userId);

    // Find a user by their email
    Optional<UserEntity> findByEmail(String email);

    // Check if an email exists
    boolean existsByEmail(String email);
}
