package com.g1appdev.mealplanner.controller;

import com.g1appdev.mealplanner.dto.PasswordChangeDTO;
import com.g1appdev.mealplanner.dto.UserProfileDTO;
import com.g1appdev.mealplanner.entity.UserEntity;
import com.g1appdev.mealplanner.service.UserService;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true", methods = { RequestMethod.GET,
        RequestMethod.PUT, RequestMethod.OPTIONS })
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private UserService userProfileService;

    @GetMapping("/print")
    public Map<String, String> getGreeting() {
        return Collections.singletonMap("message", "Hello from Spring Boot!");
    }

    @PutMapping("/profile")
    public ResponseEntity<String> updateUserProfile(@AuthenticationPrincipal UserEntity user,
            @RequestBody UserProfileDTO userProfileDTO) {
        userProfileService.updateUserProfile(user.getUserId(), userProfileDTO);
        return ResponseEntity.ok("User profile updated successfully");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserEntity user,
            @RequestBody PasswordChangeDTO passwordChangeDTO) {

        if (user == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized access.");
        }
        userProfileService.changeUserPassword(user.getUserId(), passwordChangeDTO);
        return ResponseEntity.ok("Password updated successfully");
    }

    @PostMapping("/upload-image")
    public ResponseEntity<Map<String, String>> uploadProfileImage(@AuthenticationPrincipal UserEntity user,
                                                                  @RequestParam("image") MultipartFile image) {
        try {
            String fileName = userProfileService.uploadProfileImage(user.getUserId(), image);
            Map<String, String> response = new HashMap<>();
            response.put("fileName", fileName); // 👈 return key "fileName"
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
