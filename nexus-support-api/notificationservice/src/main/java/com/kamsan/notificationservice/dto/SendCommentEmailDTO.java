package com.kamsan.notificationservice.dto;

public record SendCommentEmailDTO(
        String name,
        String email,
        String comment,
        String ticketTitle,
        String ticketNumber,
        String priority,
        String date
) {
}
