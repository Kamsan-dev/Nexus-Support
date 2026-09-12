package com.kamsan.userservice.dto;

public record PageTicketRequestDTO(
        int page,
        int size,
        String status,
        String type,
        String filter
) {
}
