package com.kamsan.ticketservice.dto;
import com.kamsan.ticketservice.enumeration.TicketType;

public record TicketByTypeDTO(Long ticketCount, TicketType type) {
}
