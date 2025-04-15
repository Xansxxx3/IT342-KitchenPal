package com.g1appdev.mealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.g1appdev.mealplanner.entity.RatingEntity;

public interface RatingRepository extends JpaRepository<RatingEntity, Integer> {
    // Additional query methods, if needed, can be defined here
}
