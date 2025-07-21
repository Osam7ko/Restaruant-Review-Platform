package com.osproject.restaurant.repository;

import com.osproject.restaurant.domain.entity.RestaurantEntity;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RestaurantRepository extends ElasticsearchRepository<RestaurantEntity, String> {

    // TODO: Custom Queries

}