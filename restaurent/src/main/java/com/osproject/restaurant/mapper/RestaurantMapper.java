package com.osproject.restaurant.mapper;

import com.osproject.restaurant.domain.RestaurantCreateUpdateRequest;
import com.osproject.restaurant.domain.dto.GeoPointDto;
import com.osproject.restaurant.domain.dto.RestaurantCreateUpdateRequestDto;
import com.osproject.restaurant.domain.dto.RestaurantDto;
import com.osproject.restaurant.domain.dto.RestaurantSummaryDto;
import com.osproject.restaurant.domain.entity.RestaurantEntity;
import com.osproject.restaurant.domain.entity.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RestaurantMapper {

    RestaurantCreateUpdateRequest toRestaurantCreateUpdateRequest(RestaurantCreateUpdateRequestDto dto);

    RestaurantDto toRestaurantDto(RestaurantEntity restaurant);


    @Mapping(source = "reviews", target = "totalReviews", qualifiedByName = "populateTotalReviews")
    RestaurantSummaryDto toSummaryDto(RestaurantEntity restaurant);

    @Named("populateTotalReviews")
    default Integer populateTotalReviews(List<ReviewEntity> reviews) {
        return reviews.size();
    }


    @Mapping(target = "latitude", expression = "java(geoPoint.getLat())")
    @Mapping(target = "longitude", expression = "java(geoPoint.getLon())")
    GeoPointDto toGeoPointDto(GeoPoint geoPoint);
}