package com.g1appdev.mealplanner.authenticator;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.g1appdev.mealplanner.entity.Role;
import com.g1appdev.mealplanner.entity.UserEntity;
import com.g1appdev.mealplanner.repository.UserRepository;
import com.g1appdev.mealplanner.config.jwtService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


   @Component
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final jwtService jwtUtil;
    private final UserRepository userRepo;

    public OAuth2LoginSuccessHandler(jwtService jwtUtil, UserRepository userRepo) {
        this.jwtUtil = jwtUtil;
        this.userRepo = userRepo;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("OAuth2 User Attributes: " + oAuth2User.getAttributes());
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("given_name");
        System.out.println(name);

        Optional<UserEntity> existingUser = userRepo.findByEmail(email);
        UserEntity user;
if (existingUser.isPresent()) { 
    user = existingUser.get();  
} else {
    user = new UserEntity();
    user.setEmail(email);
    user.setFName(name);
    user.setRole(Role.USER);
    userRepo.save(user);
}

        // Generate JWT token for this user
        String jwtToken = jwtUtil.generateToken(user);
        response.setHeader("Authorization", "Bearer " + jwtToken);
        String redirectUrl;

        System.out.println("request: " + request);
        System.out.println("resp0nse: " + response);

        
        String userAgent = request.getHeader("User-Agent");

        if (userAgent != null && (userAgent.contains("Mobile") || userAgent.contains("Android") || userAgent.contains("iPhone"))) {
            // Mobile device
            redirectUrl = "myapp://oauth2redirect?token=" + jwtToken + "&role=" + user.getRole() + "&userId=" + user.getUserId();
        } else {
            // Web client
            redirectUrl = "https://it-342-kitchen-pal-53jd.vercel.app/oauth2-redirect?token=" + jwtToken + "&role=" + user.getRole() + "&userId=" + user.getUserId();
        }
        response.sendRedirect(redirectUrl);
    }
}
