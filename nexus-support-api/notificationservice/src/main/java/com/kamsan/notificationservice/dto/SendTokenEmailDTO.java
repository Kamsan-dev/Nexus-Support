package com.kamsan.notificationservice.dto;

public record SendTokenEmailDTO(
        String name,
        String email,
        String token) {
}
