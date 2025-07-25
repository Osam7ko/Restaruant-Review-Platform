package com.osproject.restaurant.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddressDto {

    @NotBlank(message = "Street number is required")
    @Pattern(regexp = "^[0-9]{1,5}[a-zA-Z]?$", message = "Invalid street number format")
    private String streetNumber;

    @NotBlank(message = "Street name is required")
    private String streetName;

    private String unit;

    @NotBlank(message = "City is required")
    private String city;

    private String state;

    private String postalCode;

    @NotBlank(message = "Country name is required")
    private String country;

}