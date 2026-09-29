package com.kamsan.notificationservice.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SendMessageDTO(
        @Email String toEmail,
        @NotBlank String subject,
        @NotBlank String content) {
}
