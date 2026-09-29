package com.kamsan.userservice.dto;

import com.kamsan.userservice.enumeration.TicketPriority;
import com.kamsan.userservice.enumeration.TicketStatus;
import com.kamsan.userservice.enumeration.TicketType;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

public record TicketReportDTO(
        @NotNull UUID ticketPublicId,
        String title,
        String description,
        TicketStatus status,
        TicketPriority priority,
        TicketType type,
        OffsetDateTime dueDate,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt) implements Serializable {
}