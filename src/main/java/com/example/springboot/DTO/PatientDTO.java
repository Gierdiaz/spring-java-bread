package com.example.springboot.DTO;

import java.time.LocalDate;

import com.example.springboot.models.Address;
import com.example.springboot.models.Patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record PatientDTO(
    @NotBlank(message = "Name is required") String name,
    @NotBlank(message = "CPF is required") @Pattern(regexp = "\\d{11}", message = "CPF must be 11 digits") String cpf, // Validando CPF como 11 dígitos
    @NotNull(message = "Birth date is required") LocalDate birthDate,
    @NotBlank(message = "Gender is required") String gender,
    @Email(message = "Email is invalid") String email, 
    @NotBlank(message = "Phone number is required") String phone,
    @NotBlank(message = "Blood type is required") String bloodType,
    @NotNull(message = "Address is required") AddressDTO address, // Incluindo o AddressDTO para validar o endereço
    String insurance // Pode ser opcional
) {
    public Patient toPatient() {
        Address addressModel = new Address(
            address.street(),
            address.number(),
            address.complement(),
            address.neighborhood(),
            address.city(),
            address.state(),
            address.postalCode()
        );
        
        return new Patient(name, cpf, birthDate, gender, email, phone, bloodType, insurance, addressModel);
    }
}
