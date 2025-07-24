package com.osproject.restaurant.services;

import com.osproject.restaurant.domain.RestaurantCreateUpdateRequest;
import com.osproject.restaurant.domain.entity.RestaurantEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;


public interface RestaurantService {

    RestaurantEntity createRestaurant(RestaurantCreateUpdateRequest request);

    Page<RestaurantEntity> searchRestaurants(
            String query,
            Float minRating,
            Float latitude,
            Float longitude,
            Float radius,
            Pageable pageable
    );

    Optional<RestaurantEntity> getRestaurant(String id);

    RestaurantEntity updateRestaurant(String id, RestaurantCreateUpdateRequest restaurant);

    void deleteRestaurant(String id);
}