package com.kamsan.userservice.dto;

import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.UUID;

public record DoResetPasswordDTO(
        @UUID
        String token,
        @NotEmpty(message = "Field cannot be empty or null")
        String password,
        @NotEmpty(message = "Field cannot be empty or null")
        String confirmPassword) {
}
