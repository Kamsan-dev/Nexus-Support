package com.kamsan.userservice.dto;

import jakarta.validation.constraints.NotEmpty;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

public record CreateTicketDTO(
        @NotEmpty String title,
        @NotEmpty String description,
        String priority,
        String type,
        List<MultipartFile> files) implements Serializable {
}