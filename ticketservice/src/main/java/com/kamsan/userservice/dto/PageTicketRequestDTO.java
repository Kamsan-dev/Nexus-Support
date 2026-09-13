package com.kamsan.userservice.dto;

import org.springframework.data.domain.Pageable;

public record PageTicketRequestDTO(
        Pageable page,
        String status,
        String type,
        String filter
) {
}
