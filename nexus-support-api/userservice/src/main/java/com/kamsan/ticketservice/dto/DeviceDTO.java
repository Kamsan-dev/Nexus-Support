package com.kamsan.ticketservice.dto;

import java.time.OffsetDateTime;

public record DeviceDTO(
        String machine,
        String client,
        String ipAddress,
        OffsetDateTime createdAt) {
}
