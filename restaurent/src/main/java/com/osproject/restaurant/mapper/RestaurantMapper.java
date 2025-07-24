package com.osproject.restaurant.mapper;

import com.osproject.restaurant.domain.RestaurantCreateUpdateRequest;
import com.osproject.restaurant.domain.dto.*;
import com.osproject.restaurant.domain.entity.Address;
import com.osproject.restaurant.domain.entity.RestaurantEntity;
import com.osproject.restaurant.domain.entity.ReviewEntity;
import org.mapstruct.*;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {

    @Mapping(source = "address", target = "address")
    RestaurantCreateUpdateRequest toRestaurantCreateUpdateRequest(RestaurantCreateUpdateRequestDto dto);

    @Mapping(source = "address", target = "address")
    RestaurantDto toRestaurantDto(RestaurantEntity restaurant);


    @Mapping(source = "address", target = "address")
    @Mapping(source = "reviews", target = "totalReviews", qualifiedByName = "populateTotalReviews")
    RestaurantSummaryDto toSummaryDto(RestaurantEntity restaurant);

    @Named("populateTotalReviews")
    default Integer populateTotalReviews(List<ReviewEntity> reviews) {
        return reviews.size();
    }

    @Mapping(target = "streetNumber", source = "streetNumber")
    @Mapping(target = "streetName", source = "streetName")
    @Mapping(target = "unit", source = "unit")
    @Mapping(target = "city", source = "city")
    @Mapping(target = "state", source = "state")
    @Mapping(target = "postalCode", source = "postalCode")
    @Mapping(target = "country", source = "country")
    Address toAddress(AddressDto dto);

    @InheritInverseConfiguration
    AddressDto toAddressDto(Address address);


    @Mapping(target = "latitude", expression = "java(geoPoint.getLat())")
    @Mapping(target = "longitude", expression = "java(geoPoint.getLon())")
    GeoPointDto toGeoPointDto(GeoPoint geoPoint);
}