package com.osproject.restaurant.domain.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantCreateUpdateRequestDto {


    @NotBlank(message = "Restaurant name is required")
    private String name;

    @NotBlank(message = "CuisineType name is required")
    private String cuisineType;

    @NotBlank(message = "Contact Information name is required")
    private String contactInformation;

    @Valid
    private AddressDto address;

    @Valid
    private OperatingHoursDto operatingHours;

    @Size(min = 1, message = "At least one photo IDs is required")
    private List<String> photoIds;
}