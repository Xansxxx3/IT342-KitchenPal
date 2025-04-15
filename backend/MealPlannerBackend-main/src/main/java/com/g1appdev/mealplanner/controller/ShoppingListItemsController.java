package com.g1appdev.mealplanner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.g1appdev.mealplanner.entity.ShoppingListItemsEntity;
import com.g1appdev.mealplanner.service.ShoppingListItemsService;

@RestController
@RequestMapping("/api/shopping-list-items")
public class ShoppingListItemsController {

    @Autowired
    private ShoppingListItemsService shoppingListItemsService;

    @GetMapping
    public List<ShoppingListItemsEntity> getAllShoppingListItems() {
        return shoppingListItemsService.getAllShoppingListItems();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ShoppingListItemsEntity> getShoppingListItemById(@PathVariable Long id) {
        ShoppingListItemsEntity shoppingListItem = shoppingListItemsService.getShoppingListItemById(id);
        return ResponseEntity.ok(shoppingListItem);
    }

    @PostMapping
    public ShoppingListItemsEntity createShoppingListItem(@RequestBody ShoppingListItemsEntity shoppingListItem) {
        return shoppingListItemsService.createShoppingListItem(shoppingListItem);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ShoppingListItemsEntity> updateShoppingListItem(@PathVariable Long id,
            @RequestBody ShoppingListItemsEntity shoppingListItemDetails) {
        ShoppingListItemsEntity updatedShoppingListItem = shoppingListItemsService.updateShoppingListItem(id,
                shoppingListItemDetails);
        return ResponseEntity.ok(updatedShoppingListItem);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteShoppingListItem(@PathVariable Long id) {
        shoppingListItemsService.deleteShoppingListItem(id);
        return ResponseEntity.noContent().build();
    }
}