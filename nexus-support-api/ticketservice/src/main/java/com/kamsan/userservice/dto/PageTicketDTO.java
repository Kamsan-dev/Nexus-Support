package com.kamsan.userservice.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;

public record PageTicketDTO(OffsetDateTime createdAt,
                            OffsetDateTime updatedAt,
                            UUID ticketPublicId,
                            String title,
                            String description,
                            @Min(0) @Max(100) int progress,
                            String status,
                            String priority,
                            String type,
                            OffsetDateTime dueDate,
                            int fileCount,
                            int commentCount) implements Serializable {
}