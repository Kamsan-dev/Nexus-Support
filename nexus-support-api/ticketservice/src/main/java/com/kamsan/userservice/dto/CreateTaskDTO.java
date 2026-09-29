package com.kamsan.userservice.dto;

import com.kamsan.userservice.enumeration.TicketStatus;

import java.util.UUID;

public record CreateTaskDTO(
        UUID ticketPublicId,
        String name,
        String description,
        TicketStatus status
) {
}
