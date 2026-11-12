package com.kamsan.notificationservice.service;

import com.kamsan.notificationservice.dto.*;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    ConversationOverviewDTO sendMessage(UUID authenticatedUser, SendMessageDTO sendMessageDTO);

    ConversationOverviewsDTO getMessages(UUID authenticatedUser);

    ConversationMessagesDTO getConversation(UUID authenticatedUser, UUID conversationId);

    List<RecipientDTO> getAllRecipients();

}
