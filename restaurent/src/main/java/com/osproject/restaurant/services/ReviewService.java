package com.osproject.restaurant.services;

import com.osproject.restaurant.domain.ReviewCreateUpdateRequest;
import com.osproject.restaurant.domain.entity.ReviewEntity;
import com.osproject.restaurant.domain.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewService {
    ReviewEntity createReview(User author, String restaurantId, ReviewCreateUpdateRequest review);

    Page<ReviewEntity> getRestaurantReviews(String restaurantId, Pageable pageable);

    Optional<ReviewEntity> getRestaurantReview(String restaurantId, String reviewId);

    ReviewEntity updateReview(User user, String restaurantId, String reviewId,
                              ReviewCreateUpdateRequest updatedReview);

    void deleteReview(String restaurantId, String reviewId);

}