package com.g1appdev.mealplanner.config;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.g1appdev.mealplanner.entity.UserEntity;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class jwtService {

    // Secret key to sign the token (ensure this key is securely stored)
    private static final String SECRET_KEY = "gWtwQIhiImoVfcOikyCTJIuI6HiKTYrp3Em7sk6cBh8";

    // Extract username (email) from the token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject); // Username is the subject, typically email
    }

    // Generic method to extract claims (data) from the token
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // Generate a new JWT token for the given UserDetails (email and other details)
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    // Generate token with additional claims (if any)
    private String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        UserEntity user = (UserEntity) userDetails;
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername()) // Subject is typically the email
                .claim("roles", user.getRole().name())
                .setIssuedAt(new Date(System.currentTimeMillis())) // Issue time
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 24)) // Expiration time (24 hours)
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Sign the token with the secret key
                .compact();
    }

    // Validate if the token is valid by checking the username and expiration
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    // Check if the token has expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Extract the expiration time from the token
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Extract all claims from the token (the body)
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) // Use the secret key to decode the token
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Get the signing key for the JWT token (using the secret key)
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY); // Decode the secret key from Base64
        return Keys.hmacShaKeyFor(keyBytes); // Create HMAC key
    }

    // Extract the email (or username) from the token (we use the subject here)
    public String getUserEmailFromToken(String token) {
        return extractUsername(token); // Use the username (email) extracted from the token
    }
}
