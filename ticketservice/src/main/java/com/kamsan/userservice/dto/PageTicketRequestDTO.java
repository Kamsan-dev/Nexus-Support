package com.kamsan.userservice.dto;

import com.kamsan.userservice.enumeration.TicketStatus;
import com.kamsan.userservice.enumeration.TicketType;
import org.springframework.data.domain.Pageable;

public record PageTicketRequestDTO(
        Pageable page,
        TicketStatus status,
        TicketType type,
        String filter
) {
}
