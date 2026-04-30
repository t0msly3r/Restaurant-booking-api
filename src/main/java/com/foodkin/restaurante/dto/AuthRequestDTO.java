package com.foodkin.restaurante.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @Email(message = "Email in not valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 6, message = "Password must have at least 6 characters")
    @NotBlank(message = "Password is required")
    private String password;
}
