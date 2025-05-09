package com.g1appdev.mealplanner.controller;

import java.util.List;
import java.util.Map;

import com.g1appdev.mealplanner.dto.ShoppingListItemDTO;
import com.g1appdev.mealplanner.entity.ShoppingListItemEntity;
import com.g1appdev.mealplanner.entity.UserEntity;
import com.g1appdev.mealplanner.service.ShoppingListService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/shopping-list")
public class ShoppingListController {

    @Autowired
    private ShoppingListService service;

    // 1) Add a recipe’s ingredients to the current user’s list
    @PostMapping("/add-recipe/{recipeId}")
    public ResponseEntity<Void> addRecipe(
            @PathVariable int recipeId,
            @AuthenticationPrincipal UserEntity user
    ) {
        service.addRecipeToList(recipeId, user.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2) Get full list (with recipe metadata for detail modal)
    @GetMapping
    public List<ShoppingListItemDTO> getList(
            @AuthenticationPrincipal UserEntity user
    ) {
        return service.getList(user.getUserId()).stream()
                .map(item -> {
                    var dto = new ShoppingListItemDTO();
                    dto.setId(item.getId());
                    dto.setName(item.getName());
                    dto.setQuantity(item.getQuantity());
                    dto.setChecked(item.isChecked());
                    dto.setNotes(item.getNotes());

                    if (item.getRecipe() != null) {
                        dto.setRecipeId(item.getRecipe().getRecipeId());
                        dto.setRecipeTitle(item.getRecipe().getTitle());
                        dto.setRecipeImagePath(item.getRecipe().getImagePath());
                        dto.setRecipeDescription(item.getRecipe().getDescription());
                        dto.setRecipeIngredients(item.getRecipe().getIngredients());
                    }

                    return dto;
                })
                .toList();
    }

    // 3) Update a single item (check/uncheck or edit)
    @PutMapping("/item/{itemId}")
    public ShoppingListItemEntity updateItem(
            @PathVariable Long itemId,
            @RequestBody Map<String, Object> body
    ) {
        Boolean checked  = (Boolean) body.get("checked");
        String  name     = (String)  body.get("name");
        String  quantity = (String)  body.get("quantity");
        String  notes    = (String)  body.get("notes");

        return service.updateItem(itemId, checked, name, quantity, notes);
    }

    // 4) Clear all checked
    @DeleteMapping("/clear-checked")
    public ResponseEntity<Void> clearChecked(
            @AuthenticationPrincipal UserEntity user
    ) {
        service.clearChecked(user.getUserId());
        return ResponseEntity.noContent().build();
    }
}
