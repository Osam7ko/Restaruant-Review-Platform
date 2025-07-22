package com.osproject.restaurant.services.impl;

import com.osproject.restaurant.domain.GeoLocation;
import com.osproject.restaurant.domain.entity.Address;
import com.osproject.restaurant.services.GeoLocationService;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomRiyadhGeoLocationService implements GeoLocationService {

    // Riyadh
    private static final float MIN_LATITUDE = 24.3f;
    private static final float MAX_LATITUDE = 25.0f;
    private static final float MIN_LONGITUDE = 46.3f;
    private static final float MAX_LONGITUDE = 47.1f;


    @Override
    public GeoLocation geoLocate(Address address) {
        Random random = new Random();
        double latitude = MIN_LATITUDE + random.nextDouble() * (MAX_LATITUDE - MIN_LATITUDE);
        double longitude = MIN_LONGITUDE + random.nextDouble() * (MAX_LONGITUDE - MIN_LONGITUDE);

        return GeoLocation.builder()
                .latitude(latitude)
                .longitude(longitude)
                .build();
    }
}