package com.kamsan.userservice.dto;

import com.kamsan.userservice.enumeration.TicketPriority;
import com.kamsan.userservice.enumeration.TicketStatus;
import com.kamsan.userservice.enumeration.TicketType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * DTO for {@link com.kamsan.userservice.model.Ticket}
 */
public record TicketDTO(OffsetDateTime createdAt,
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