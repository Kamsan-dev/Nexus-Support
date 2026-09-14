package com.kamsan.userservice.dto;

import java.util.UUID;

public record CreateCommentDTO(
        UUID ticketPublicId,
        String comment
) {
}
