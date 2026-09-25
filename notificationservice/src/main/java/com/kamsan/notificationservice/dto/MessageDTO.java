package com.kamsan.notificationservice.dto;

import java.time.OffsetDateTime;
import java.util.UUID;

public record MessageDTO(
        UUID senderPublicId,
        String senderFirstname,
        String senderLastname,
        String senderEmail,
        String senderImageUrl,
        UUID receiverPublicId,
        String receiverFirstname,
        String receiverLastname,
        String receiverEmail,
        String receiverImageUrl,
        UUID messagePublicId,
        String subject,
        String content,
        String status,
        OffsetDateTime createdAt,
        OffsetDateTime updatedAt
) {
}
