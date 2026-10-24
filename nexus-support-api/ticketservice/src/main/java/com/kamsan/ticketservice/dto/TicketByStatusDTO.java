package com.kamsan.ticketservice.dto;

import com.kamsan.ticketservice.enumeration.TicketStatus;

public record TicketByStatusDTO(Long ticketCount, TicketStatus status) {
}
