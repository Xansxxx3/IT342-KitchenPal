package com.g1appdev.mealplanner.dto;

import java.util.List;

public class ShoppingListItemDTO {
    private Long id;
    private String name;
    private String quantity;
    private boolean checked;
    private String notes;

    // Recipe metadata for the detail modal
    private Integer recipeId;
    private String recipeTitle;
    private String recipeImagePath;
    private String recipeDescription;
    private List<String> recipeIngredients;

    // Default constructor
    public ShoppingListItemDTO() {}

    // All-args constructor
    public ShoppingListItemDTO(
            Long id,
            String name,
            String quantity,
            boolean checked,
            String notes,
            Integer recipeId,
            String recipeTitle,
            String recipeImagePath,
            String recipeDescription,
            List<String> recipeIngredients
    ) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.checked = checked;
        this.notes = notes;
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.recipeImagePath = recipeImagePath;
        this.recipeDescription = recipeDescription;
        this.recipeIngredients = recipeIngredients;
    }

    // Getters & Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(Integer recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeImagePath() {
        return recipeImagePath;
    }

    public void setRecipeImagePath(String recipeImagePath) {
        this.recipeImagePath = recipeImagePath;
    }

    public String getRecipeDescription() {
        return recipeDescription;
    }

    public void setRecipeDescription(String recipeDescription) {
        this.recipeDescription = recipeDescription;
    }

    public List<String> getRecipeIngredients() {
        return recipeIngredients;
    }

    public void setRecipeIngredients(List<String> recipeIngredients) {
        this.recipeIngredients = recipeIngredients;
    }
}
