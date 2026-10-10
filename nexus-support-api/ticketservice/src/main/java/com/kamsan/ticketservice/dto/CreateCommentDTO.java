package com.kamsan.ticketservice.dto;

import java.util.UUID;

public record CreateCommentDTO(
        UUID ticketPublicId,
        String comment
) {
}
