package com.g1appdev.mealplanner.controller;

import com.g1appdev.mealplanner.entity.RatingEntity;
import com.g1appdev.mealplanner.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rating")
public class RatingController {

    @Autowired
    RatingService ratingService;

    @PostMapping("/add")
    public ResponseEntity<RatingEntity> postRating(@RequestBody RatingEntity rating) {
        try {
            RatingEntity createdRating = ratingService.postRating(rating);
            return new ResponseEntity<>(createdRating, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<RatingEntity>> getAllRatings() {
        List<RatingEntity> ratings = ratingService.getAllRatings();
        if (ratings.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RatingEntity> getRatingById(@PathVariable int id) {
        Optional<RatingEntity> ratingData = ratingService.getRatingById(id);
        return ratingData.map(rating -> new ResponseEntity<>(rating, HttpStatus.OK))
                         .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RatingEntity> updateRating(@PathVariable int id, @RequestBody RatingEntity newRatingDetails) {
        try {
            RatingEntity updatedRating = ratingService.updateRating(id, newRatingDetails);
            return new ResponseEntity<>(updatedRating, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<HttpStatus> deleteRatingById(@PathVariable int id) {
        try {
            ratingService.deleteRatingById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
