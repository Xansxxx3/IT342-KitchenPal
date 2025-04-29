package com.g1appdev.mealplanner.service;

import com.g1appdev.mealplanner.entity.CommentEntity;
import com.g1appdev.mealplanner.entity.MealplanEntity;
import com.g1appdev.mealplanner.entity.UserEntity;
import com.g1appdev.mealplanner.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    /**
     * Fetch all comments for a given meal plan
     */
    public List<CommentEntity> getCommentsForMealPlan(MealplanEntity mealPlan) {
        return commentRepository.findByMealPlan(mealPlan);
    }

    /**
     * Add a comment to a meal plan from a user
     */
    public CommentEntity addComment(UserEntity user, MealplanEntity mealPlan, String text) {
        CommentEntity comment = new CommentEntity();
        comment.setUser(user);
        comment.setMealPlan(mealPlan);
        comment.setText(text);
        return commentRepository.save(comment);
    }

    @Transactional
    public void deleteComment(Long commentId, UserEntity user, MealplanEntity mealPlan) {
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        // Check if the comment belongs to the authenticated user and to the correct meal plan
        if (comment.getUser().getUserId() != user.getUserId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can only delete your own comments.");
        }
        if (comment.getMealPlan().getMealPlanId() != mealPlan.getMealPlanId()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment does not belong to this meal plan.");
        }

        commentRepository.delete(comment);
    }

}
