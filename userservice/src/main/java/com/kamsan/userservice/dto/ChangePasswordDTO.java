package com.kamsan.userservice.dto;

import jakarta.validation.constraints.NotEmpty;

public record ChangePasswordDTO(
        @NotEmpty(message = "Field cannot be empty or null")
        String currentPassword,
        @NotEmpty(message = "Field cannot be empty or null")
        String newPassword,
        @NotEmpty(message = "Field cannot be empty or null")
        String confirmNewPassword) {
}
