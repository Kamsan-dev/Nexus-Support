package com.kamsan.notificationservice.dto;

import java.util.List;
import java.util.UUID;

public record ConversationMessagesDTO(
        List<MessageDTO> messages,
        long messageStatusUpdatedCount,
        UUID correspondentPublicId
) {}