package com.g1appdev.mealplanner.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.g1appdev.mealplanner.entity.Dish;
import com.g1appdev.mealplanner.repository.DishRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class DishService {

    @Autowired
    private DishRepository dishRepository;

    // Retrieve all dishes
    public List<Dish> getAllDishes() {
        return dishRepository.findAll();
    }

    // Retrieve a dish by ID
    public Dish getDishById(int id) { // Changed from Long to int
        return dishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dish not found with ID: " + id));
    }

    // Create a new dish
    public Dish createDish(Dish dish) {
        return dishRepository.save(dish);
    }

    // Update an existing dish
    public Dish updateDish(int id, Dish dishDetails) { // Changed from Long to int
        Dish dish = dishRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Dish not found with ID: " + id));

        dish.setTitle(dishDetails.getTitle());
        dish.setImage(dishDetails.getImage());
        return dishRepository.save(dish);
    }

    // Delete a dish by ID
    public void deleteDish(int id) { // Changed from Long to int
        if (!dishRepository.existsById(id)) {
            throw new EntityNotFoundException("Dish not found with ID: " + id);
        }
        dishRepository.deleteById(id);
    }
}
