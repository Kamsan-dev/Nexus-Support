package com.kamsan.ticketservice.dto;

import com.kamsan.ticketservice.enumeration.TicketPriority;
import com.kamsan.ticketservice.enumeration.TicketStatus;
import com.kamsan.ticketservice.enumeration.TicketType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.kamsan.ticketservice.model.Ticket}
 */
public record TicketDetailsDTO(OffsetDateTime createdAt,
                               OffsetDateTime updatedAt,
                               @NotNull UUID ticketPublicId,
                               @NotNull UUID issuerPublicId,
                               UUID assigneePublicId,
                               String title,
                               String description,
                               @Min(0) @Max(100) int progress,
                               TicketStatus status,
                               TicketPriority priority,
                               TicketType type,
                               OffsetDateTime dueDate) implements Serializable {
}