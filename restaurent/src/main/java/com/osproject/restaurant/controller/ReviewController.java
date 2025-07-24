package com.osproject.restaurant.controller;

import com.osproject.restaurant.domain.ReviewCreateUpdateRequest;
import com.osproject.restaurant.domain.dto.ReviewCreateUpdateRequestDto;
import com.osproject.restaurant.domain.dto.ReviewDto;
import com.osproject.restaurant.domain.entity.ReviewEntity;
import com.osproject.restaurant.domain.entity.User;
import com.osproject.restaurant.mapper.ReviewMapper;
import com.osproject.restaurant.services.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/restaurants/{restaurantId}/reviews")
public class ReviewController {
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable String restaurantId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto review,
            @AuthenticationPrincipal Jwt jwt) {
// Convert the review DTO to a domain object
        ReviewCreateUpdateRequest ReviewCreateUpdateRequest =
                reviewMapper.toReviewCreateUpdateRequest(review);
// Extract user details from JWT
        User user = jwtToUser(jwt);
// Create the review
        ReviewEntity createdReview = reviewService.createReview(
                user, restaurantId, ReviewCreateUpdateRequest);
// Return the created review as DTO
        return ResponseEntity.ok(reviewMapper.toDto(createdReview));
    }

    private User jwtToUser(Jwt jwt) {
        return new User(
                jwt.getSubject(), // User's unique ID
                jwt.getClaimAsString("preferred_username"), // Username
                jwt.getClaimAsString("given_name"), // First name
                jwt.getClaimAsString("family_name") // Last name
        );
    }

    @GetMapping
    public Page<ReviewDto> listReviews(
            @PathVariable String restaurantId,
            @PageableDefault(size = 20, page = 0, sort = "datePosted", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return reviewService
                .getRestaurantReviews(restaurantId, pageable)
                .map(reviewMapper::toDto);
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> getRestaurantReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId) {
        return reviewService
                .getRestaurantReview(restaurantId, reviewId)
                .map(reviewMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto review,
            @AuthenticationPrincipal Jwt jwt) {

        ReviewCreateUpdateRequest reviewCreateUpdateRequest =
                reviewMapper.toCreateUpdateReviewRequest(review);
        User user = jwtToUser(jwt);
        ReviewEntity updatedReview = reviewService.updateReview(
                user,
                restaurantId,
                reviewId,
                reviewCreateUpdateRequest);
        return ResponseEntity.ok(reviewMapper.toDto(updatedReview));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String restaurantId,
            @PathVariable String reviewId
    ) {
        reviewService.deleteReview(restaurantId, reviewId);
        return ResponseEntity.noContent().build();
    }


}