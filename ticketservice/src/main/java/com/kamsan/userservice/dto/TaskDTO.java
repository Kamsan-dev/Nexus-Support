package com.kamsan.userservice.dto;

import com.kamsan.userservice.enumeration.TicketStatus;

import java.time.OffsetDateTime;
import java.util.UUID;

public record TaskDTO(
        UUID taskPublicId,
        UUID ticketPublicId,
        UUID assigneePublicId,
        String name,
        String description,
        OffsetDateTime dueDate,
        TicketStatus status,
        String assigneeFirstName,
        String assigneeLastName,
        String assigneeImageUrl,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) {
}
