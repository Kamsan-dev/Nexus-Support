package com.kamsan.notificationservice.dto;

import java.util.List;

public record ConversationOverviewsDTO(List<MessageDTO> conversations,
                                       Long unreadMessagesCount) {
}
