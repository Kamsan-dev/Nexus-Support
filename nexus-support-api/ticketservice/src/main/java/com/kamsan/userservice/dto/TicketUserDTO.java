package com.kamsan.userservice.dto;

import com.kamsan.userservice.enumeration.Role;

import java.util.UUID;

public record TicketUserDTO(UUID userPublicId,
                            String email,
                            String firstName,
                            String lastName,
                            String imageUrl,
                            Role role) {
}