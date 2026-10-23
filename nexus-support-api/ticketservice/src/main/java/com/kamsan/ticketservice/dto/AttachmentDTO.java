package com.kamsan.ticketservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class AttachmentDTO {

    private UUID ticketPublicId;
    private UUID filePublicId;
    private String name;
    private Long size;
    private String formattedSize;
    private String extension;
    private String uri;
    private OffsetDateTime createdAt;

}