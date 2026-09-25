package com.kamsan.notificationservice.service.implementation;

import com.kamsan.notificationservice.dto.SendMessageDTO;
import com.kamsan.notificationservice.model.Message;
import com.kamsan.notificationservice.service.NotificationService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    @Override
    public Message sendMessage(SendMessageDTO sendMessageDTO) {
        
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getMessages(UUID userPublicId) {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Message> getConversation(UUID userPublicId, String conversationId) {
        return List.of();
    }

    @Override
    @Transactional(readOnly = true)
    public String getMessageStatus(UUID authenticatedUser, Long messageId) {
        return "";
    }

    @Override
    public void updateMessageStatus(UUID authenticatedUser, Long messageId, String status) {

    }
}
