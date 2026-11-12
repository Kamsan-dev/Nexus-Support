package com.kamsan.notificationservice.dto;

import java.util.UUID;

public record RecipientDTO(

        UUID userPublicId,
        String firstName,
        String lastName,
        String imageUrl,
        String role

) {
}
