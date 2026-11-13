package com.kamsan.notificationservice.service;

import com.kamsan.notificationservice.dto.*;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    MessageDTO sendMessage(UUID authenticatedUser, SendMessageDTO sendMessageDTO);

    MessageDTO sendMessageInConversation(UUID authenticatedUser, SendMessageDTO sendMessageDTO, UUID conversationId);

    ConversationOverviewsDTO getMessages(UUID authenticatedUser);

    ConversationMessagesDTO getConversation(UUID authenticatedUser, UUID conversationId);

    List<RecipientDTO> getAllRecipients(UUID authenticatedUser);

}
