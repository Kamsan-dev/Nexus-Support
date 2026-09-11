package com.kamsan.userservice.dto;

import java.util.UUID;

public record CreateTaskDTO(
        UUID ticketPublicId,
        String name,
        String description,
        String status
) {
}
