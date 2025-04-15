package com.g1appdev.mealplanner.entity;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tblshoppinglistitems")
public class ShoppingListItemsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long shoppingListItemId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToMany
    @JoinTable(name = "shopping_list_item_recipes", joinColumns = @JoinColumn(name = "shopping_list_item_id"), inverseJoinColumns = @JoinColumn(name = "recipe_id"))
    private Set<RecipeEntity> recipes;

    private int quantity;
    private String status;

    public ShoppingListItemsEntity() {
        super();
    }

    public Long getShoppingListItemId() {
        return shoppingListItemId;
    }

    public void setShoppingListItemId(Long shoppingListItemId) {
        this.shoppingListItemId = shoppingListItemId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public Set<RecipeEntity> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<RecipeEntity> recipes) {
        this.recipes = recipes;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}