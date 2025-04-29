package com.g1appdev.mealplanner.controller;
import com.g1appdev.mealplanner.dto.CommentRequestDTO;
import com.g1appdev.mealplanner.dto.CommentResponseDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.stream.Collectors;
import com.g1appdev.mealplanner.entity.MealplanEntity;
import com.g1appdev.mealplanner.entity.CommentEntity;
import com.g1appdev.mealplanner.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;

import java.util.List;
import com.g1appdev.mealplanner.service.CommentService;
import com.g1appdev.mealplanner.repository.MealplanRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
;

@RestController
@RequestMapping("/api/meal-plans")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private MealplanRepository mealPlanRepository;

    // GET /api/meal-plans/{mealPlanId}/comments
    @GetMapping("/{mealPlanId}/comments")
    public ResponseEntity<List<CommentResponseDTO>> getComments(@PathVariable Long mealPlanId) {
        MealplanEntity mealPlan = mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        List<CommentResponseDTO> dtos = commentService
                .getCommentsForMealPlan(mealPlan)
                .stream()
                .map(comment -> new CommentResponseDTO(
                        comment.getId(),  // commentId
                        comment.getUser().getUserId(),  // userId
                        comment.getText(),
                        comment.getUser().getFName(),
                        comment.getUser().getLName(),
                        comment.getUser().getProfileImagePath(),
                        comment.getCreatedAt()
                ))
                .collect(Collectors.toList());


        return ResponseEntity.ok(dtos);
    }

    // POST /api/meal-plans/{mealPlanId}/comments
    @PostMapping("/{mealPlanId}/comments")
    public ResponseEntity<CommentResponseDTO> postComment(
            @PathVariable Long mealPlanId,
            @RequestBody CommentRequestDTO request,
            @AuthenticationPrincipal UserEntity user
    ) {
        MealplanEntity mealPlan = mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        CommentEntity saved = commentService.addComment(user, mealPlan, request.getText());

        CommentResponseDTO dto = new CommentResponseDTO(
                saved.getId(),  // commentId
                user.getUserId(),  // userId
                saved.getText(),
                user.getFName(),
                user.getLName(),
                user.getProfileImagePath(),
                saved.getCreatedAt()
        );


        return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @DeleteMapping("/{mealPlanId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long mealPlanId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal UserEntity user
    ) {
        MealplanEntity mealPlan = mealPlanRepository.findById(mealPlanId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        commentService.deleteComment(commentId, user, mealPlan);

        return ResponseEntity.noContent().build();
    }

}
