package com.g1appdev.mealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.g1appdev.mealplanner.entity.ShoppingListItemsEntity;

public interface ShoppingListItemsRepository extends JpaRepository<ShoppingListItemsEntity, Long> {

}