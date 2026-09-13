package com.kamsan.userservice.dto;

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
                        UUID issuerPublicId,
                        UUID assigneePublicId,
                        String title,
                        String description,
                        @Min(0) @Max(100) int progress,
                        String status,
                        String priority,
                        String typeId,
                        OffsetDateTime dueDate) implements Serializable {
}