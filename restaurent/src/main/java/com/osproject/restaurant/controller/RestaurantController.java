package com.osproject.restaurant.controller;

import com.osproject.restaurant.domain.RestaurantCreateUpdateRequest;
import com.osproject.restaurant.domain.dto.RestaurantCreateUpdateRequestDto;
import com.osproject.restaurant.domain.dto.RestaurantDto;
import com.osproject.restaurant.domain.entity.RestaurantEntity;
import com.osproject.restaurant.mapper.RestaurantMapper;
import com.osproject.restaurant.services.RestaurantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantMapper restaurantMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<RestaurantDto> createRestaurant(@Valid @RequestBody RestaurantCreateUpdateRequestDto request) {
        RestaurantCreateUpdateRequest restaurantCreateUpdateRequest = restaurantMapper
                .toRestaurantCreateUpdateRequest(request);

        RestaurantEntity restaurant = restaurantService.createRestaurant(restaurantCreateUpdateRequest);
        RestaurantDto createdRestaurant = restaurantMapper.toRestaurantDto(restaurant);
        return ResponseEntity.ok(createdRestaurant);
    }
}