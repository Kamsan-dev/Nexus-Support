package com.kamsan.notificationservice.dto;

public record SendTokenEmailDTO(
        String name,
        String to,
        String token) {
}
