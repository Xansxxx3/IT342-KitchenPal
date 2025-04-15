package com.g1appdev.mealplanner.entity;  

import jakarta.persistence.Entity;  
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity  
@Table(name = "tblfavoriterecipe")  
public class FavoriteRecipeEntity {  

    @Id  
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    private Long favoriteRecipeId;  

    @ManyToOne  
    @JoinColumn(name = "user_id", nullable = false)  
    private UserEntity user;  

    @ManyToOne  
    @JoinColumn(name = "recipe_id", nullable = false)  
    private RecipeEntity recipe;  

    

    public Long getFavoriteRecipeId() {  
        return favoriteRecipeId;  
    }  

    public void setFavoriteRecipeId(Long favoriteRecipeId) {  
        this.favoriteRecipeId = favoriteRecipeId;  
    }  

    public UserEntity getUser() {  
        return user;  
    }  

    public void setUser(UserEntity user) {  
        this.user = user;  
    }  

    public RecipeEntity getRecipe() {  
        return recipe;  
    }  

    public void setRecipe(RecipeEntity recipe) {  
        this.recipe = recipe;  
    }  
}