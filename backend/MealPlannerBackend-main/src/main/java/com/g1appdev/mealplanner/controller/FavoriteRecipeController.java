package com.g1appdev.mealplanner.controller;  

import java.util.List;  

import org.springframework.beans.factory.annotation.Autowired;  
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g1appdev.mealplanner.entity.FavoriteRecipeEntity;
import com.g1appdev.mealplanner.service.FavoriteRecipeService;

@RestController  
@RequestMapping("/api/favorite-recipes")  
public class FavoriteRecipeController {  

    @Autowired  
    private FavoriteRecipeService favoriteRecipeService;  

    @GetMapping  
    public ResponseEntity<List<FavoriteRecipeEntity>> getAllFavoriteRecipes() {  
        return ResponseEntity.ok(favoriteRecipeService.getAllFavoriteRecipes());  
    }  

    @GetMapping("/{id}")  
    public ResponseEntity<FavoriteRecipeEntity> getFavoriteRecipeById(@PathVariable Long id) {  
        return ResponseEntity.ok(favoriteRecipeService.getFavoriteRecipeById(id));  
    }  

    @PostMapping  
    public ResponseEntity<FavoriteRecipeEntity> createFavoriteRecipe(@RequestBody FavoriteRecipeEntity favoriteRecipe) {  
        FavoriteRecipeEntity createdRecipe = favoriteRecipeService.createFavoriteRecipe(favoriteRecipe);  
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRecipe);  
    }  

    @PutMapping("/{id}")  
    public ResponseEntity<FavoriteRecipeEntity> updateFavoriteRecipe(  
            @PathVariable Long id, @RequestBody FavoriteRecipeEntity favoriteRecipeDetails) {  
        FavoriteRecipeEntity updatedRecipe = favoriteRecipeService.updateFavoriteRecipe(id, favoriteRecipeDetails);  
        return ResponseEntity.ok(updatedRecipe);  
    }  

    @DeleteMapping("/{id}")  
    public ResponseEntity<Void> deleteFavoriteRecipe(@PathVariable Long id) {  
        favoriteRecipeService.deleteFavoriteRecipe(id);  
        return ResponseEntity.noContent().build();  
    }  
}