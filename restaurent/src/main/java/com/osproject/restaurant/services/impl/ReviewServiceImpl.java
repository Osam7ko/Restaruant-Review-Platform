package com.osproject.restaurant.services.impl;

import com.osproject.restaurant.domain.ReviewCreateUpdateRequest;
import com.osproject.restaurant.domain.entity.PhotoEntity;
import com.osproject.restaurant.domain.entity.RestaurantEntity;
import com.osproject.restaurant.domain.entity.ReviewEntity;
import com.osproject.restaurant.domain.entity.User;
import com.osproject.restaurant.exceptions.RestaurantNotFoundException;
import com.osproject.restaurant.exceptions.ReviewNotAllowedException;
import com.osproject.restaurant.repository.RestaurantRepository;
import com.osproject.restaurant.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final RestaurantRepository restaurantRepository;

    @Override
    public ReviewEntity createReview(User author, String restaurantId, ReviewCreateUpdateRequest createReviewRequest) {
        RestaurantEntity restaurant = getRestaurantOrThrow(restaurantId);

        boolean hasExistingReview = restaurant.getReviews().stream()
                .anyMatch(r -> r.getWrittenBy().getId().equals(author.getId()));
        if (hasExistingReview) {
            throw new ReviewNotAllowedException("User has already reviewed this restaurant");
        }

        LocalDateTime now = LocalDateTime.now();

        List<PhotoEntity> photos = createReviewRequest.getPhotoIds().stream()
                .map(url -> {
                    PhotoEntity photo = new PhotoEntity();
                    photo.setUrl(url);
                    photo.setUploadDate(now);
                    return photo;
                })
                .collect(Collectors.toList());

        ReviewEntity review = ReviewEntity.builder()
                .id(UUID.randomUUID().toString())
                .content(createReviewRequest.getContent())
                .rating(createReviewRequest.getRating())
                .photos(photos)
                .datePosted(now)
                .lastEdited(now)
                .writtenBy(author)
                .build();

        restaurant.getReviews().add(review);
        updateRestaurantAverageRating(restaurant);
        RestaurantEntity updatedRestaurant = restaurantRepository.save(restaurant);

        return updatedRestaurant.getReviews().stream()
                .filter(r -> r.getDatePosted().equals(review.getDatePosted()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Error retrieving created review"));
    }

    private RestaurantEntity getRestaurantOrThrow(String restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant not found with id: " + restaurantId));
    }

    private void updateRestaurantAverageRating(RestaurantEntity restaurant) {
        List<ReviewEntity> reviews = restaurant.getReviews();
        if (reviews.isEmpty()) {
            restaurant.setAverageRating(0.0f);
        } else {
            float averageRating = (float) reviews.stream()
                    .mapToDouble(ReviewEntity::getRating)
                    .average()
                    .orElse(0.0);
            restaurant.setAverageRating(averageRating);
        }
    }


    @Override
    public Page<ReviewEntity> getRestaurantReviews(String restaurantId, Pageable pageable) {
        RestaurantEntity restaurant = getRestaurantOrThrow(restaurantId);
        List<ReviewEntity> reviews = new ArrayList<>(restaurant.getReviews());
        Sort sort = pageable.getSort();
        if (sort.isSorted()) {
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            boolean isAscending = order.getDirection().isAscending();
            Comparator<ReviewEntity> comparator = switch (property) {
                case "datePosted" -> Comparator.comparing(ReviewEntity::getDatePosted);
                case "rating" -> Comparator.comparing(ReviewEntity::getRating);
                default -> Comparator.comparing(ReviewEntity::getDatePosted);
            };
            reviews.sort(isAscending ? comparator : comparator.reversed());
        } else {
            reviews.sort(Comparator.comparing(ReviewEntity::getDatePosted).reversed());
        }
        int start = (int) pageable.getOffset();
        if (start >= reviews.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, reviews.size());
        }
        int end = Math.min((start + pageable.getPageSize()), reviews.size());
        return new PageImpl<>(reviews.subList(start, end), pageable, reviews.size());
    }

    @Override
    public Optional<ReviewEntity> getRestaurantReview(String restaurantId, String reviewId) {
        RestaurantEntity restaurant = getRestaurantOrThrow(restaurantId);
        return restaurant.getReviews().stream()
                .filter(r -> reviewId.equals(r.getId()))
                .findFirst();
    }

    @Override
    public ReviewEntity updateReview(User user, String restaurantId, String reviewId,
                                     ReviewCreateUpdateRequest updatedReview) {
        RestaurantEntity restaurant = getRestaurantOrThrow(restaurantId);
        String currentUserId = user.getId();
        List<ReviewEntity> reviews = restaurant.getReviews();
        ReviewEntity existingReview = reviews.stream()
                .filter(r -> r.getId().equals(reviewId) &&
                        r.getWrittenBy().getId().equals(currentUserId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Review not found"));
        if (LocalDateTime.now().isAfter(existingReview.getDatePosted().plusHours(48))) {
            throw new ReviewNotAllowedException("Review can no longer be edited (48-hour limit exceeded)");
        }

        existingReview.setContent(updatedReview.getContent());
        existingReview.setRating(updatedReview.getRating());
        existingReview.setLastEdited(LocalDateTime.now());
        existingReview.setPhotos(updatedReview.getPhotoIds().stream()
                .map(url -> {
                    PhotoEntity photo = new PhotoEntity();
                    photo.setUrl(url);
                    photo.setUploadDate(LocalDateTime.now());
                    return photo;
                }).collect(Collectors.toList()));
        updateRestaurantAverageRating(restaurant);

        RestaurantEntity savedRestaurant = restaurantRepository.save(restaurant);
        return savedRestaurant.getReviews().stream()
                .filter(r -> r.getId().equals(reviewId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Error retrieving updated review"));
    }

    @Override
    public void deleteReview(String restaurantId, String reviewId) {
// Get the restaurant or throw an exception if not found
        RestaurantEntity restaurant = getRestaurantOrThrow(restaurantId);
// Filter out the review with the matching ID
        List<ReviewEntity> filteredReviews = restaurant.getReviews().stream()
                .filter(review -> !reviewId.equals(review.getId()))
                .toList();
// Update the restaurant's reviews
        restaurant.setReviews(filteredReviews);
// Update the restaurant's average rating
        updateRestaurantAverageRating(restaurant);
// Save the updated restaurant
        restaurantRepository.save(restaurant);
    }


}