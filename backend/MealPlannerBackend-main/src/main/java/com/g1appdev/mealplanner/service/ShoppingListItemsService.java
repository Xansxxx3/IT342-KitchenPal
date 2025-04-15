package com.g1appdev.mealplanner.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g1appdev.mealplanner.entity.RecipeEntity;
import com.g1appdev.mealplanner.entity.ShoppingListItemsEntity;
import com.g1appdev.mealplanner.repository.ShoppingListItemsRepository;

@Service
public class ShoppingListItemsService {

    @Autowired
    private ShoppingListItemsRepository shoppingListItemsRepository;

    public List<ShoppingListItemsEntity> getAllShoppingListItems() {
        return shoppingListItemsRepository.findAll();
    }

    public ShoppingListItemsEntity getShoppingListItemById(Long id) {
        return shoppingListItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shopping List Item not found with id: " + id));
    }

    public ShoppingListItemsEntity createShoppingListItem(ShoppingListItemsEntity shoppingListItem) {
        return shoppingListItemsRepository.save(shoppingListItem);
    }

    public ShoppingListItemsEntity updateShoppingListItem(Long id, ShoppingListItemsEntity shoppingListItemDetails) {
        ShoppingListItemsEntity shoppingListItem = shoppingListItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shopping List Item not found with id: " + id));

        shoppingListItem.setUser(shoppingListItemDetails.getUser());
        shoppingListItem.setQuantity(shoppingListItemDetails.getQuantity());
        shoppingListItem.setStatus(shoppingListItemDetails.getStatus());

        shoppingListItem.setRecipes(shoppingListItemDetails.getRecipes());

        return shoppingListItemsRepository.save(shoppingListItem);
    }

    public void deleteShoppingListItem(Long id) {
        ShoppingListItemsEntity shoppingListItem = shoppingListItemsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Shopping List Item not found with id: " + id));

        shoppingListItemsRepository.delete(shoppingListItem);
    }

    public void addRecipeToShoppingListItem(Long shoppingListItemId, RecipeEntity recipe) {
        ShoppingListItemsEntity shoppingListItem = getShoppingListItemById(shoppingListItemId);
        shoppingListItem.getRecipes().add(recipe);
        shoppingListItemsRepository.save(shoppingListItem);
    }

    public void removeRecipeFromShoppingListItem(Long shoppingListItemId, RecipeEntity recipe) {
        ShoppingListItemsEntity shoppingListItem = getShoppingListItemById(shoppingListItemId);
        shoppingListItem.getRecipes().remove(recipe);
        shoppingListItemsRepository.save(shoppingListItem);
    }
}