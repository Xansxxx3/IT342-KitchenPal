package com.g1appdev.mealplanner.service;  

import java.util.List;  

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.stereotype.Service;

import com.g1appdev.mealplanner.entity.FavoriteRecipeEntity;
import com.g1appdev.mealplanner.entity.UserEntity;
import com.g1appdev.mealplanner.repository.FavoriteRecipeRepository;
import com.g1appdev.mealplanner.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;  

@Service  
public class FavoriteRecipeService {  

    @Autowired  
    private FavoriteRecipeRepository favoriteRecipeRepository;  

    @Autowired  
    private UserRepository userRepository;  

    public List<FavoriteRecipeEntity> getAllFavoriteRecipes() {  
        return favoriteRecipeRepository.findAll();  
    }  

    public FavoriteRecipeEntity getFavoriteRecipeById(Long id) {  
        return favoriteRecipeRepository.findById(id)  
                .orElseThrow(() -> new EntityNotFoundException("Favorite Recipe not found"));  
    }  

    public FavoriteRecipeEntity createFavoriteRecipe(FavoriteRecipeEntity favoriteRecipe) {  
        UserEntity user = userRepository.findById(favoriteRecipe.getUser().getUserId())  
                .orElseThrow(() -> new EntityNotFoundException("User not found"));  
        favoriteRecipe.setUser(user);  
        return favoriteRecipeRepository.save(favoriteRecipe);  
    }  

    public FavoriteRecipeEntity updateFavoriteRecipe(Long id, FavoriteRecipeEntity favoriteRecipeDetails) {  
        FavoriteRecipeEntity favoriteRecipe = favoriteRecipeRepository.findById(id)  
                .orElseThrow(() -> new EntityNotFoundException("Favorite Recipe not found"));  

        UserEntity user = userRepository.findById(favoriteRecipeDetails.getUser().getUserId())  
                .orElseThrow(() -> new EntityNotFoundException("User not found"));  
        favoriteRecipe.setUser(user);  
        favoriteRecipe.setRecipe(favoriteRecipeDetails.getRecipe());  
        
        return favoriteRecipeRepository.save(favoriteRecipe);  
    }  

    public void deleteFavoriteRecipe(Long id) {  
        if (!favoriteRecipeRepository.existsById(id)) {  
            throw new EntityNotFoundException("Favorite Recipe not found");  
        }  
        favoriteRecipeRepository.deleteById(id);  
    }  
}