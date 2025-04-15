package com.g1appdev.mealplanner.service;

import com.g1appdev.mealplanner.dto.PasswordChangeDTO;
import com.g1appdev.mealplanner.dto.UserProfileDTO;
import com.g1appdev.mealplanner.entity.UserEntity;
import com.g1appdev.mealplanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Update user profile (for admin and user)
    public void updateUserProfile(long userId, UserProfileDTO userProfileDTO) {
        // Fetch the user from the database by user ID
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Update user details
        user.setFName(userProfileDTO.getFname());
        user.setLName(userProfileDTO.getLname());
        user.setEmail(userProfileDTO.getEmail());

        // Save the updated user entity to the database
        userRepository.save(user);
    }

    // Change user password (for admin and user)
    public void changeUserPassword(long userId, PasswordChangeDTO passwordChangeDTO) {
        // Fetch the user from the database by user ID
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        // Validate current password
        if (!passwordEncoder.matches(passwordChangeDTO.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect.");
        }

        // Set the new password (encoded)
        user.setPassword(passwordEncoder.encode(passwordChangeDTO.getNewPassword()));

        // Save the updated user entity to the database
        userRepository.save(user);
    }

    // Delete user by admin (delete operation)
    public List<UserEntity> getAllUsers() {
        return userRepository.findAll(); // Fetch all users from the repository
    }

    // Get user by ID
    public UserEntity getUserById(long id) {
        Optional<UserEntity> userOpt = userRepository.findById(id);
        return userOpt.orElse(null); // Return user if present, otherwise return null
    }

    // Update user
    public UserEntity updateUser(long id, UserEntity updatedUser) {
        Optional<UserEntity> userOpt = userRepository.findById(id);
        if (userOpt.isPresent()) {
            UserEntity user = userOpt.get();
            // Update fields
            user.setFName(updatedUser.getFName());
            user.setLName(updatedUser.getLName());
            user.setEmail(updatedUser.getEmail());
            user.setRole(updatedUser.getRole());

            // Save updated user
            return userRepository.save(user);
        }
        return null; // Return null if the user is not found
    }

    // Delete user
    public boolean deleteUser(long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true; // Return true if deletion is successful
        }
        return false; // Return false if the user does not exist
    }

}
