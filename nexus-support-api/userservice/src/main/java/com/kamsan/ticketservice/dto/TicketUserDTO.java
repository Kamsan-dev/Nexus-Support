package com.kamsan.ticketservice.dto;

import java.util.UUID;

public record TicketUserDTO(UUID userPublicId,
                            String email,
                            String firstName,
                            String lastName,
                            String imageUrl,
                            String role) {
}
