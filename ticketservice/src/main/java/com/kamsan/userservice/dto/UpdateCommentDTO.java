package com.kamsan.userservice.dto;

import java.util.UUID;

public record UpdateCommentDTO(
        UUID ownerCommentPublicId,
        UUID commentPublicId,
        String comment) {
}
