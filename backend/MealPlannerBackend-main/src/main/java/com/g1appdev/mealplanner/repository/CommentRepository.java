package com.g1appdev.mealplanner.repository;

import com.g1appdev.mealplanner.entity.CommentEntity;
import com.g1appdev.mealplanner.entity.MealplanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByMealPlan(MealplanEntity mealPlan);
}