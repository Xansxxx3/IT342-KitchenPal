package com.g1appdev.mealplanner.repository;  

import org.springframework.data.jpa.repository.JpaRepository;

import com.g1appdev.mealplanner.entity.FavoriteRecipeEntity;  

public interface FavoriteRecipeRepository extends JpaRepository<FavoriteRecipeEntity, Long> {  
}