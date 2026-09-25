package com.kamsan.notificationservice.dto;

public record SendMessageDTO(
        String toEmail,
        String subject,
        String content) {
}
