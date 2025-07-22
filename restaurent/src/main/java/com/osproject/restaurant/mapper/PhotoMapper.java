package com.osproject.restaurant.mapper;

import com.osproject.restaurant.domain.dto.PhotoDto;
import com.osproject.restaurant.domain.entity.PhotoEntity;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {

    PhotoDto toDto(PhotoEntity photo);

}