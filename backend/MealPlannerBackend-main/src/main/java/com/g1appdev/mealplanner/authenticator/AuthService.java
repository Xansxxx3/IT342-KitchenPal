package com.g1appdev.mealplanner.authenticator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.g1appdev.mealplanner.config.jwtService;
import com.g1appdev.mealplanner.entity.Role;
import com.g1appdev.mealplanner.entity.UserEntity;
import com.g1appdev.mealplanner.repository.UserRepository;

@Service
public class AuthService {

    @Autowired
    private UserRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private jwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthService(AuthenticationManager authenticationManager, UserRepository repository, jwtService jwtService,
            PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.repository = repository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    // Register new user
    public AuthenticationResponse register(RegisterRequest request) {
        System.out.println("Registering user: " + request.getEmail());

        // Check if user already exists
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email is already registered.");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setFName(request.getFname());
        userEntity.setLName(request.getLname());
        userEntity.setEmail(request.getEmail());
        userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        userEntity.setRole(Role.USER);

        // Save the user to the database
        repository.save(userEntity);

        // Generate JWT token
        String jwtToken = jwtService.generateToken(userEntity);
        return new AuthenticationResponse(jwtToken, userEntity.getRole().name(), userEntity.getUserId());
    }

    public AuthenticationResponse registerAdmin(RegisterRequest request) {
        System.out.println("Registering admin: " + request.getEmail());

        // Check if user already exists
        if (repository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalStateException("Email is already registered.");
        }

        UserEntity adminEntity = new UserEntity();
        adminEntity.setFName(request.getFname());
        adminEntity.setLName(request.getLname());
        adminEntity.setEmail(request.getEmail());
        adminEntity.setPassword(passwordEncoder.encode(request.getPassword()));
        adminEntity.setRole(Role.ADMIN); // Set role to ADMIN

        // Save the admin to the database
        repository.save(adminEntity);

        // Generate JWT token
        String jwtToken = jwtService.generateToken(adminEntity);
        return new AuthenticationResponse(jwtToken, adminEntity.getRole().name(), adminEntity.getUserId());
    }

    // Authenticate existing user (login)
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // Authenticate user
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        // Find the user in the database
        UserEntity user = repository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Generate JWT token
        String token = jwtService.generateToken(user);

        // Return token, role, and userId
        return new AuthenticationResponse(token, user.getRole().name(), user.getUserId());
    }

    // Method to fetch user profile by decoding JWT token
    public UserEntity getUserProfile(String token) {
        // Decode the token and get the email (or any unique identifier)
        String email = jwtService.getUserEmailFromToken(token);

        // Fetch the user from the repository
        UserEntity user = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return user; // Return the user profile
    }
}
