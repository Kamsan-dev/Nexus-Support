package com.kamsan.ticketservice.dto;

import com.kamsan.ticketservice.enumeration.TicketPriority;
import com.kamsan.ticketservice.enumeration.TicketType;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public record CreateTicketDTO(
        @NotEmpty String title,
        @NotEmpty String description,
        TicketPriority priority,
        TicketType type) implements Serializable {
}