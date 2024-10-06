package com.example.springboot.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressDTO(
    @NotBlank(message = "Street is required") String street,
    @NotNull(message = "Number is required") Integer number,
    String complement,
    @NotBlank(message = "Neighborhood is required") String neighborhood,
    @NotBlank(message = "City is required") String city,
    @NotBlank(message = "State is required") String state,
    @NotBlank(message = "Postal code is required") String postalCode
) {
}
