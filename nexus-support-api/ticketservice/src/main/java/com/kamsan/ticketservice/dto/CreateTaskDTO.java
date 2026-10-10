package com.kamsan.ticketservice.dto;

import com.kamsan.ticketservice.enumeration.TicketStatus;

import java.util.UUID;

public record CreateTaskDTO(
        UUID ticketPublicId,
        String name,
        String description,
        TicketStatus status
) {
}
