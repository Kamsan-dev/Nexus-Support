package com.kamsan.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class MessageDTO {

    private UUID senderPublicId;
    private String senderFirstname;
    private String senderLastname;
    private String senderEmail;
    private String senderImageUrl;

    private UUID receiverPublicId;
    private String receiverFirstname;
    private String receiverLastname;
    private String receiverEmail;
    private String receiverImageUrl;

    private Long messageId;
    private UUID messagePublicId;
    private String subject;
    private String content;
    private String status;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
