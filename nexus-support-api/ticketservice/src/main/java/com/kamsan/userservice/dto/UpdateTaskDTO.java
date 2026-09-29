package com.kamsan.userservice.dto;

import com.kamsan.userservice.enumeration.TicketStatus;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UpdateTaskDTO(
        UUID taskPublicId,
        UUID assigneePublicId,
        @NotBlank String name,
        @NotBlank String description,
        TicketStatus status) {
}
