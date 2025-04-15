package com.g1appdev.mealplanner.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g1appdev.mealplanner.entity.RecipeEntity;
import com.g1appdev.mealplanner.repository.RecipeRepository;

import java.util.List;

@Service
public class RecipeService {

    @Autowired
    RecipeRepository rrepo;

    public RecipeService() {
        super();
    }

    public RecipeEntity postRecipe(RecipeEntity recipe) {
        return rrepo.save(recipe);
    }

    public List<RecipeEntity> getAllRecipes() {
        return rrepo.findAll();
    }

    public Optional<RecipeEntity> getRecipeById(int id) {
        return rrepo.findById(id);
    }

    public RecipeEntity putRecipeDetails(int id, RecipeEntity newRecipeDetails) {
        try {
            RecipeEntity existingRecipe = rrepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Recipe with ID " + id + " not found."));

            existingRecipe.setTitle(newRecipeDetails.getTitle());
            existingRecipe.setDescription(newRecipeDetails.getDescription());
            existingRecipe.setIngredients(newRecipeDetails.getIngredients());
            existingRecipe.setPrepTime(newRecipeDetails.getPrepTime());
            existingRecipe.setNutritionInfo(newRecipeDetails.getNutritionInfo());
            existingRecipe.setCuisineType(newRecipeDetails.getCuisineType());
            existingRecipe.setMealType(newRecipeDetails.getMealType());
            existingRecipe.setRatingsAverage(newRecipeDetails.getRatingsAverage());

            return rrepo.save(existingRecipe);
        } catch (Exception e) {
            throw new RuntimeException("Error updating recipe with ID " + id + ": " + e.getMessage());
        }
    }

    public void deleteRecipeById(int id) {
        rrepo.deleteById(id);
    }

    public List<RecipeEntity> findByCuisineType(String cuisineType) {
        return rrepo.findByCuisineType(cuisineType);
    }

    public List<RecipeEntity> findByMealType(String mealType) {
        return rrepo.findByMealType(mealType);
    }

    public List<RecipeEntity> findByIngredients(String ingredients) {
        return rrepo.findByIngredients(ingredients);
    }
}
