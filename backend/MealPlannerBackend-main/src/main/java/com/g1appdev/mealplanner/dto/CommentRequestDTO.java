package com.g1appdev.mealplanner.dto;

public class CommentRequestDTO {
    private String text;

    public CommentRequestDTO() {}

    public CommentRequestDTO(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
