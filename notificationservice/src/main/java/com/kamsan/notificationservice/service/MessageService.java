package com.kamsan.notificationservice.service;

import com.kamsan.notificationservice.dto.MessageDTO;
import com.kamsan.notificationservice.dto.SendMessageDTO;

import java.util.List;
import java.util.UUID;

public interface MessageService {

    UUID sendMessage(UUID authenticatedUser, SendMessageDTO sendMessageDTO);

    List<MessageDTO> getMessages(UUID authenticatedUser);

    List<MessageDTO> getConversation(UUID authenticatedUser, String conversationId);

    String getMessageStatus(UUID authenticatedUser, Long messageId);

    String updateMessageStatus(UUID authenticatedUser, Long messageId, String status);

}
