package com.kamsan.ticketservice.dto;

import com.kamsan.ticketservice.enumeration.TicketStatus;
import com.kamsan.ticketservice.enumeration.TicketType;
import org.springframework.data.domain.Pageable;

public record PageTicketRequestDTO(
        Pageable page,
        TicketStatus status,
        TicketType type,
        String filter
) {
}
