package com.kamsan.userservice.dto;

import java.util.UUID;

public record AttachmentDTO(
        UUID ticketPublicId,
        String name,
        Long size,
        String formattedSize,
        String extension,
        String uri
) {
}
