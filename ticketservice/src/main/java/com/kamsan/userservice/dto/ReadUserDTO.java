package com.kamsan.userservice.dto;

import com.kamsan.userservice.enumeration.Role;

import java.time.OffsetDateTime;
import java.util.UUID;

public record ReadUserDTO(UUID userPublicId,
                          String email,
                          String firstName,
                          String lastName,
                          String memberId,
                          String bio,
                          String imageUrl,
                          String phone,
                          String address,
                          String qrCodeImageUri,
                          boolean isUsingMfa,
                          OffsetDateTime lastLogin,
                          OffsetDateTime createdAt,
                          OffsetDateTime updatedAt,
                          Role role,
                          String authorities,
                          boolean isAccountExpired,
                          boolean isAccountLocked,
                          boolean isAccountEnabled) {
}
