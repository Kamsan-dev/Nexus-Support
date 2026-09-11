package com.kamsan.userservice.dto;

import org.hibernate.validator.constraints.UUID;

public record RequestPageTicketDTO(
        @UUID
        UUID userPublicId,
        int page,
        int size,
        String status,
        String type,
        String filter
) {
}
