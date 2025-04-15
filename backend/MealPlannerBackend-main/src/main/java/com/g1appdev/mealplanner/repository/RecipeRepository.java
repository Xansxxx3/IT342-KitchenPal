package com.g1appdev.mealplanner.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.g1appdev.mealplanner.entity.RecipeEntity;

public interface RecipeRepository extends JpaRepository<RecipeEntity, Integer> {

    List<RecipeEntity> findByCuisineType(String cuisineType);

    List<RecipeEntity> findByMealType(String mealType);

    List<RecipeEntity> findByIngredients(String ingredients);
}
