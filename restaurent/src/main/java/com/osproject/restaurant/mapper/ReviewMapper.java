package com.osproject.restaurant.mapper;

import com.osproject.restaurant.domain.ReviewCreateUpdateRequest;
import com.osproject.restaurant.domain.dto.ReviewCreateUpdateRequestDto;
import com.osproject.restaurant.domain.dto.ReviewDto;
import com.osproject.restaurant.domain.entity.ReviewEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {
    ReviewCreateUpdateRequest toReviewCreateUpdateRequest(ReviewCreateUpdateRequestDto dto);

    ReviewDto toDto(ReviewEntity review);

    ReviewCreateUpdateRequest toCreateUpdateReviewRequest(ReviewCreateUpdateRequestDto review);
}