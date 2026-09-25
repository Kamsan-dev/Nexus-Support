package com.kamsan.notificationservice.service;

import com.kamsan.notificationservice.dto.SendMessageDTO;
import com.kamsan.notificationservice.model.Message;

import java.util.List;
import java.util.UUID;

public interface NotificationService {

    Message sendMessage(SendMessageDTO sendMessageDTO);

    List<Message> getMessages(UUID authenticatedUser);

    List<Message> getConversation(UUID authenticatedUser, String conversationId);

    String getMessageStatus(UUID authenticatedUser, Long messageId);

    void updateMessageStatus(UUID authenticatedUser, Long messageId, String status);

}
