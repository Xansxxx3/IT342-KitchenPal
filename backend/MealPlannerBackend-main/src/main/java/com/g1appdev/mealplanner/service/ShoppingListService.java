package com.g1appdev.mealplanner.service;

import java.util.List;

import com.g1appdev.mealplanner.entity.ShoppingListItemEntity;
import com.g1appdev.mealplanner.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.g1appdev.mealplanner.entity.RecipeEntity;
import com.g1appdev.mealplanner.repository.ShoppingListItemRepository;
import com.g1appdev.mealplanner.repository.RecipeRepository;
import org.springframework.web.server.ResponseStatusException;

// ShoppingListService.java
@Service
public class ShoppingListService {

    @Autowired
    private ShoppingListItemRepository repo;

    @Autowired
    private RecipeRepository recipeRepo;

    @Autowired
    private UserRepository userRepo;

    @Transactional
    public void addRecipeToList(int recipeId, Long userId) {
        // Check if this recipe is already added to the user's shopping list
        boolean alreadyAdded = repo.existsByUserUserIdAndRecipeRecipeId(userId, recipeId);
        if (alreadyAdded) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Recipe already in shopping list");
        }

        RecipeEntity recipe = recipeRepo.findById(recipeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Recipe not found"));

        var user = userRepo.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "User not found"));

        for (String ing : recipe.getIngredients()) {
            var item = new ShoppingListItemEntity();
            item.setUser(user);
            item.setRecipe(recipe);
            item.setName(ing);
            repo.save(item);
        }
    }


    public List<ShoppingListItemEntity> getList(Long userId) {
        return repo.findByUserUserIdOrderByCheckedAscNameAsc(userId);
    }

    public ShoppingListItemEntity updateItem(
            Long itemId,
            Boolean checked,
            String name,
            String quantity,
            String notes) {

        ShoppingListItemEntity item = repo.findById(itemId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Item not found"));

        if (checked != null)  item.setChecked(checked);
        if (name != null)     item.setName(name);
        if (quantity != null) item.setQuantity(quantity);
        if (notes != null)    item.setNotes(notes);

        return repo.save(item);
    }

    @Transactional
    public void clearChecked(Long userId) {
        repo.deleteByUserUserIdAndCheckedTrue(userId);
    }
}

