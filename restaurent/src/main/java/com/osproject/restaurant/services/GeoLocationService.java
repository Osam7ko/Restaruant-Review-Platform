package com.osproject.restaurant.services;

import com.osproject.restaurant.domain.GeoLocation;
import com.osproject.restaurant.domain.entity.Address;

public interface GeoLocationService {

    GeoLocation geoLocate(Address address);
}