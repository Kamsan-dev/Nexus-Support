package com.kamsan.userservice.dto;

import com.kamsan.userservice.enumeration.TicketPriority;
import com.kamsan.userservice.enumeration.TicketType;
import jakarta.validation.constraints.NotEmpty;

import java.io.Serializable;

public record CreateTicketDTO(
        @NotEmpty String title,
        @NotEmpty String description,
        TicketPriority priority,
        TicketType type) implements Serializable {
}