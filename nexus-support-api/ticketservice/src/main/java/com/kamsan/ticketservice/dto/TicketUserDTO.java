package com.kamsan.ticketservice.dto;

import com.kamsan.ticketservice.enumeration.Role;

import java.util.UUID;

public record TicketUserDTO(UUID userPublicId,
                            String email,
                            String firstName,
                            String lastName,
                            String imageUrl,
                            Role role) {
}