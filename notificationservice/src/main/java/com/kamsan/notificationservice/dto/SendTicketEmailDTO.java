package com.kamsan.notificationservice.dto;

public record SendTicketEmailDTO(
        String name,
        String email,
        String ticketTitle,
        String ticketNumber,
        String priority
) {
}
