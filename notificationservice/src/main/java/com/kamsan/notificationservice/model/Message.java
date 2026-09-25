package com.kamsan.notificationservice.model;

import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "messages")
public class Message {
    private Long messageId;
    private UUID message_public_id;
    private String conversationId;
    private String subject;
    private String content;
    private Long senderId;
    private Long receiverId;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
}
