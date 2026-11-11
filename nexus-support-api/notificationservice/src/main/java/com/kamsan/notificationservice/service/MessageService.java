package com.kamsan.notificationservice.service;

import com.kamsan.notificationservice.dto.ConversationMessagesDTO;
import com.kamsan.notificationservice.dto.ConversationOverviewsDTO;
import com.kamsan.notificationservice.dto.MessageDTO;
import com.kamsan.notificationservice.dto.SendMessageDTO;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    UUID sendMessage(UUID authenticatedUser, SendMessageDTO sendMessageDTO);

    ConversationOverviewsDTO getMessages(UUID authenticatedUser);

    ConversationMessagesDTO getConversation(UUID authenticatedUser, UUID conversationId);

}
