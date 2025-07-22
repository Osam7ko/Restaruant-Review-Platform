package com.osproject.restaurant.services;

import com.osproject.restaurant.domain.RestaurantCreateUpdateRequest;
import com.osproject.restaurant.domain.entity.RestaurantEntity;


public interface RestaurantService {

    RestaurantEntity createRestaurant(RestaurantCreateUpdateRequest request);
}