package com.g1appdev.mealplanner.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.g1appdev.mealplanner.entity.MealplanEntity;
import com.g1appdev.mealplanner.entity.UserEntity;
import java.util.List;

@Repository
public interface MealplanRepository extends JpaRepository<MealplanEntity, Long> {
    List<MealplanEntity> findByUser(UserEntity user);
}
