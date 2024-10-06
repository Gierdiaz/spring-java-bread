package com.example.springboot.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressDTO(
    @NotBlank String street,
    @NotNull Integer number,
    String complement, // Pode ser opcional
    @NotBlank String neighborhood,
    @NotBlank String city,
    @NotBlank String state,
    @NotBlank String postalCode
) {
}