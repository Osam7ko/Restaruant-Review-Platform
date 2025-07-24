package com.osproject.restaurant.services.impl;

import com.osproject.restaurant.domain.GeoLocation;
import com.osproject.restaurant.domain.RestaurantCreateUpdateRequest;
import com.osproject.restaurant.domain.entity.Address;
import com.osproject.restaurant.domain.entity.PhotoEntity;
import com.osproject.restaurant.domain.entity.RestaurantEntity;
import com.osproject.restaurant.exceptions.RestaurantNotFoundException;
import com.osproject.restaurant.repository.RestaurantRepository;
import com.osproject.restaurant.services.GeoLocationService;
import com.osproject.restaurant.services.RestaurantService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Override
    public Page<RestaurantEntity> searchRestaurants(
            String query,
            Float minRating,
            Float latitude,
            Float longitude,
            Float radius,
            Pageable pageable) {

        if (null != minRating && (null == query || query.isEmpty())) {
            return restaurantRepository.findByAverageRatingGreaterThanEqual(minRating, pageable);
        }

        Float searchMinRating = minRating == null ? 0f : minRating;
        if (query != null && !query.trim().isEmpty()) {
            return restaurantRepository.findByQueryAndMinRating(query, searchMinRating, pageable);
        }
        if (latitude != null && longitude != null && radius != null) {
            return restaurantRepository.findByLocationNear(latitude, longitude, radius, pageable);
        }
        return restaurantRepository.findAll(pageable);
    }

    @Override
    public Optional<RestaurantEntity> getRestaurant(String id) {
        return restaurantRepository.findById(id);
    }


    @Override
    public RestaurantEntity updateRestaurant(String id, RestaurantCreateUpdateRequest restaurantCreateUpdateRequest) {

        RestaurantEntity existingRestaurant = getRestaurant(id)
                .orElseThrow(() -> new RestaurantNotFoundException("Restaurant with ID " + id + " does not exist"));

        GeoLocation newGeoLocation = geoLocationService.geoLocate(restaurantCreateUpdateRequest.getAddress());
        GeoPoint newGeoPoint = new GeoPoint(newGeoLocation.getLatitude(), newGeoLocation.getLongitude());

        List<PhotoEntity> photos = restaurantCreateUpdateRequest.getPhotoIds().stream()
                .map(photoUrl -> PhotoEntity.builder()
                        .url(photoUrl)
                        .uploadDate(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        existingRestaurant.setName(restaurantCreateUpdateRequest.getName());
        existingRestaurant.setCuisineType(restaurantCreateUpdateRequest.getCuisineType());
        existingRestaurant.setContactInformation(restaurantCreateUpdateRequest.getContactInformation());
        existingRestaurant.setAddress(restaurantCreateUpdateRequest.getAddress());
        existingRestaurant.setGeoLocation(newGeoPoint);
        existingRestaurant.setOperatingHours(restaurantCreateUpdateRequest.getOperatingHours());
        existingRestaurant.setPhotos(photos);

        return restaurantRepository.save(existingRestaurant);
    }


    @Override
    public void deleteRestaurant(String id) {
        restaurantRepository.deleteById(id);
    }


}