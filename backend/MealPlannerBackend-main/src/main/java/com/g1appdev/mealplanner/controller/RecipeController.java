package com.g1appdev.mealplanner.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.g1appdev.mealplanner.entity.RecipeEntity;
import com.g1appdev.mealplanner.service.RecipeService;

@RestController
@RequestMapping("/api/recipe")
public class RecipeController {

    @Autowired
    RecipeService rserve;

    @PostMapping("/addrecipe")
    public ResponseEntity<RecipeEntity> postRecipe(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("ingredients") String ingredients,
            @RequestParam("prepTime") int prepTime,
            @RequestParam("nutritionInfo") String nutritionInfo,
            @RequestParam("cuisineType") String cuisineType,
            @RequestParam("mealType") String mealType,
            @RequestParam("ratingsAverage") double ratingsAverage,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            RecipeEntity recipe = new RecipeEntity();
            recipe.setTitle(title);
            recipe.setDescription(description);
            recipe.setIngredients(ingredients);
            recipe.setPrepTime(prepTime);
            recipe.setNutritionInfo(nutritionInfo);
            recipe.setCuisineType(cuisineType);
            recipe.setMealType(mealType);
            recipe.setRatingsAverage(ratingsAverage);

            if (image != null) {
                String fileName = image.getOriginalFilename();
                Path path = Paths.get("uploads/" + fileName);
                Files.write(path, image.getBytes());
                recipe.setImagePath(fileName); // Save only the filename
            }

            RecipeEntity savedRecipe = rserve.postRecipe(recipe);
            return new ResponseEntity<>(savedRecipe, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RecipeEntity> updateRecipe(
            @PathVariable int id,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("ingredients") String ingredients,
            @RequestParam("prepTime") int prepTime,
            @RequestParam("nutritionInfo") String nutritionInfo,
            @RequestParam("cuisineType") String cuisineType,
            @RequestParam("mealType") String mealType,
            @RequestParam("ratingsAverage") double ratingsAverage,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        try {
            RecipeEntity recipe = rserve.getRecipeById(id).orElseThrow(() -> new RuntimeException("Recipe not found"));
            recipe.setTitle(title);
            recipe.setDescription(description);
            recipe.setIngredients(ingredients);
            recipe.setPrepTime(prepTime);
            recipe.setNutritionInfo(nutritionInfo);
            recipe.setCuisineType(cuisineType);
            recipe.setMealType(mealType);
            recipe.setRatingsAverage(ratingsAverage);

            if (image != null) {
                String fileName = image.getOriginalFilename();
                Path path = Paths.get("uploads/" + fileName);
                Files.write(path, image.getBytes());
                recipe.setImagePath(fileName); // Save only the filename
            }

            RecipeEntity updatedRecipe = rserve.putRecipeDetails(id, recipe);
            return new ResponseEntity<>(updatedRecipe, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/images/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            Path filePath = Paths.get("uploads").resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {
                System.out.println("Serving image: " + filename);
                return ResponseEntity.ok()
                        .header("Content-Type", Files.probeContentType(filePath))
                        .body(resource);
            } else {
                System.err.println("Image not found: " + filename);
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            System.err.println("Error serving image: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/allrecipe")
    public ResponseEntity<List<RecipeEntity>> getAllRecipes() {
        List<RecipeEntity> recipes = rserve.getAllRecipes();
        return ResponseEntity.ok(recipes); // Return the list even if empty
    }

    @GetMapping("/{id}")
    public ResponseEntity<RecipeEntity> getRecipeById(@PathVariable int id) {
        Optional<RecipeEntity> recipeData = rserve.getRecipeById(id);
        if (recipeData.isPresent()) {
            return new ResponseEntity<>(recipeData.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<RecipeEntity> deleteRecipeById(@PathVariable int id) {
        try {
            rserve.deleteRecipeById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/cuisine/{cuisineType}")
    public ResponseEntity<List<RecipeEntity>> findByCuisineType(@PathVariable String cuisineType) {
        List<RecipeEntity> recipes = rserve.findByCuisineType(cuisineType);
        if (recipes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping("/meal/{mealType}")
    public ResponseEntity<List<RecipeEntity>> findByMealType(@PathVariable String mealType) {
        List<RecipeEntity> recipes = rserve.findByMealType(mealType);
        if (recipes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }

    @GetMapping("/ingredients/{ingredients}")
    public ResponseEntity<List<RecipeEntity>> findByIngredients(@PathVariable String ingredients) {
        List<RecipeEntity> recipes = rserve.findByIngredients(ingredients);
        if (recipes.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(recipes, HttpStatus.OK);
    }
}
