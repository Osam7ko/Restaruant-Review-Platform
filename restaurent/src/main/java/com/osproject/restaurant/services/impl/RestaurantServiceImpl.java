package com.osproject.restaurant.services.impl;

import com.osproject.restaurant.domain.GeoLocation;
import com.osproject.restaurant.domain.RestaurantCreateUpdateRequest;
import com.osproject.restaurant.domain.entity.Address;
import com.osproject.restaurant.domain.entity.PhotoEntity;
import com.osproject.restaurant.domain.entity.RestaurantEntity;
import com.osproject.restaurant.repository.RestaurantRepository;
import com.osproject.restaurant.services.GeoLocationService;
import com.osproject.restaurant.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final GeoLocationService geoLocationService;

    @Override
    public RestaurantEntity createRestaurant(RestaurantCreateUpdateRequest request) {
        Address address = request.getAddress();
        GeoLocation geoLocation = geoLocationService.geoLocate(address);
        GeoPoint geoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());

        List<String> photoIds = request.getPhotoIds();
        List<PhotoEntity> photos = photoIds.stream().map(photoUrl -> PhotoEntity.builder()
                .url(photoUrl)
                .uploadDate(LocalDateTime.now())
                .build()).toList();

        RestaurantEntity restaurant = RestaurantEntity.builder()
                .name(request.getName())
                .cuisineType(request.getCuisineType())
                .contactInformation(request.getContactInformation())
                .geoLocation(geoPoint)
                .operatingHours(request.getOperatingHours())
                .averageRating(0f)
                .photos(photos)
                .build();


        return restaurantRepository.save(restaurant);
    }
}