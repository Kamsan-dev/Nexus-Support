package com.kamsan.userservice.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UpdateTicketDTO(
        UUID ticketPublicId,
        String title,
        String description,
        int progress,
        String type,
        String priority,
        String status,
        OffsetDateTime dueDate
) {
}
