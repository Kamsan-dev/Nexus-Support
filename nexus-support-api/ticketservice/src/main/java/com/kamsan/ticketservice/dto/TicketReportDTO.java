package com.kamsan.ticketservice.dto;

import com.kamsan.ticketservice.enumeration.TicketPriority;
import com.kamsan.ticketservice.enumeration.TicketStatus;
import com.kamsan.ticketservice.enumeration.TicketType;
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