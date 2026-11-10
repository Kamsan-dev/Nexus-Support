package com.kamsan.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;
import java.util.UUID;

@AllArgsConstructor
@Data
@Builder
public class ConversationOverviewDTO {

    // sender last message data
    private UUID senderPublicId;
    private String senderFirstname;
    private String senderLastname;
    private String senderEmail;
    private String senderImageUrl;

    // receiver last message data
    private UUID receiverPublicId;
    private String receiverFirstname;
    private String receiverLastname;
    private String receiverEmail;
    private String receiverImageUrl;

    // last message data
    private UUID conversationId;
    private UUID messagePublicId;
    private String title;
    private String lastMessageContent;
    private String lastMessageStatus;

    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

}
