package com.kamsan.notificationservice.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record SendMessageDTO(
        UUID receiverPublicId,
        @NotBlank String content) {
}
