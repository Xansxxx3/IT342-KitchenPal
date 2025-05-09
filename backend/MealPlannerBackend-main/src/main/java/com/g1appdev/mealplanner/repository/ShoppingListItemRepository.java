package com.g1appdev.mealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g1appdev.mealplanner.entity.ShoppingListItemEntity;

import java.util.List;

// ShoppingListItemRepository.java
public interface ShoppingListItemRepository
        extends JpaRepository<ShoppingListItemEntity, Long> {
    List<ShoppingListItemEntity> findByUserUserIdOrderByCheckedAscNameAsc(Long userId);
    void deleteByUserUserIdAndCheckedTrue(Long userId);
    boolean existsByUserUserIdAndRecipeRecipeId(Long userId, Integer recipeId);
}
