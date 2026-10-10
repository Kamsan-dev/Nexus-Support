package com.kamsan.ticketservice.dto;

import com.kamsan.ticketservice.enumeration.TicketPriority;
import com.kamsan.ticketservice.enumeration.TicketStatus;
import com.kamsan.ticketservice.enumeration.TicketType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UpdateTicketDTO(
        @NotNull UUID ticketPublicId,
        @NotNull UUID issuerPublicId,
        String title,
        String description,
        @Min(0) @Max(100) int progress,
        TicketType type,
        TicketPriority priority,
        TicketStatus status,
        OffsetDateTime dueDate
) {
}
