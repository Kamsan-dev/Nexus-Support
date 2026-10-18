package com.kamsan.ticketservice.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record CommentDTO(
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt,
        UUID commentPublicId,
        UUID userPublicId,
        UUID ticketPublicId,
        String comment,
        boolean isEdited,
        String firstName,
        String lastName,
        String imageUrl
) {
}
