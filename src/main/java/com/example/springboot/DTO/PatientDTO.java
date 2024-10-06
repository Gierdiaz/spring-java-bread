package com.example.springboot.DTO;

import java.time.LocalDate;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PatientDTO(
        @NotBlank String name,
        @NotBlank @Pattern(regexp = "\\d{11}") String cpf, // Validando CPF como 11 dígitos
        @NotNull LocalDate birthDate,
        @NotBlank String gender,
        @Email String email, 
        @NotBlank String phone,
        @NotBlank String bloodType,
        @NotNull AddressDTO address, // Incluindo o AddressDTO para validar o endereço
        String insurance // Pode ser opcional
) {
}
