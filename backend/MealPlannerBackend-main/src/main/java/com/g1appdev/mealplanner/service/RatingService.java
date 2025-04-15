package com.g1appdev.mealplanner.service;

import com.g1appdev.mealplanner.entity.RatingEntity;
import com.g1appdev.mealplanner.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {

    @Autowired
    RatingRepository ratingRepo;

    public RatingService() {
        super();
    }

    public RatingEntity postRating(RatingEntity rating) {
        return ratingRepo.save(rating);
    }

    public List<RatingEntity> getAllRatings() {
        return ratingRepo.findAll();
    }

    public Optional<RatingEntity> getRatingById(int id) {
        return ratingRepo.findById(id);
    }

    public RatingEntity updateRating(int id, RatingEntity newRatingDetails) {
        try {
            RatingEntity existingRating = ratingRepo.findById(id)
                    .orElseThrow(() -> new RuntimeException("Rating with ID " + id + " not found."));

            existingRating.setRatingValue(newRatingDetails.getRatingValue());
            existingRating.setReviewComment(newRatingDetails.getReviewComment());

            return ratingRepo.save(existingRating);
        } catch (Exception e) {
            throw new RuntimeException("Error updating rating with ID " + id + ": " + e.getMessage());
        }
    }

    public void deleteRatingById(int id) {
        ratingRepo.deleteById(id);
    }
}
