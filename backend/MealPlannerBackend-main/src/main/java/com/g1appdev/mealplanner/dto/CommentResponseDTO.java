package com.g1appdev.mealplanner.dto;

import java.time.LocalDateTime;

public class CommentResponseDTO {
    private Long commentId;
    private Long userId;
    private String text;
    private String firstName;
    private String lastName;
    private String profileImagePath;
    private LocalDateTime createdAt;

    public CommentResponseDTO() {}

    public CommentResponseDTO(Long commentId, Long userId, String text, String firstName, String lastName, String profileImagePath, LocalDateTime createdAt) {
        this.commentId = commentId;
        this.userId = userId;
        this.text = text;
        this.firstName = firstName;
        this.lastName = lastName;
        this.profileImagePath = profileImagePath;
        this.createdAt = createdAt;
    }


    public Long getCommentId() {
        return commentId;
    }

    public Long getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getProfileImagePath() {
        return profileImagePath;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}
