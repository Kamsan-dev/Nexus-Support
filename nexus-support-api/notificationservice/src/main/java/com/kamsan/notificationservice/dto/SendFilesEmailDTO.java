package com.kamsan.notificationservice.dto;

public record SendFilesEmailDTO(
        String name,
        String email,
        String files,
        String ticketTitle,
        String ticketNumber,
        String priority,
        String date
) {
}
