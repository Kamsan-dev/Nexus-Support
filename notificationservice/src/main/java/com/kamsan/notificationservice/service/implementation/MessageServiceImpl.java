package com.kamsan.notificationservice.service.implementation;

import com.kamsan.notificationservice.dto.MessageDTO;
import com.kamsan.notificationservice.dto.SendMessageDTO;
import com.kamsan.notificationservice.model.Message;
import com.kamsan.notificationservice.repository.MessageQueryRepository;
import com.kamsan.notificationservice.service.MessageService;
import com.kamsan.notificationservice.sharedkernel.exception.ApiException;
import com.kamsan.notificationservice.utils.NotificationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MessageQueryRepository messageQueryRepository;

    @Override
    @Transactional
    public UUID sendMessage(UUID authenticatedUser, SendMessageDTO sendMessageDTO) {
        UUID conversationId = messageQueryRepository.findConversationId(authenticatedUser,
                sendMessageDTO.toEmail()).orElse(NotificationUtils.randomUUID.get());

        Message message = messageQueryRepository.saveNewMessage(authenticatedUser, sendMessageDTO, conversationId);
        messageQueryRepository.createNewMessageStatus(message);
        return message.getMessagePublicId();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MessageDTO> getMessages(UUID authenticatedUser) {
        return messageQueryRepository.findMessagesByUserPublicId(authenticatedUser);
    }

    @Override
    public List<MessageDTO> getConversation(UUID authenticatedUser, String conversationId) {
        List<MessageDTO> messages = messageQueryRepository.findMessagesByConversationId(
                authenticatedUser,
                conversationId);

        messages.forEach(message -> {
            message.setStatus(updateMessageStatus(authenticatedUser, message.getMessageId(), "READ"));
            message.setMessageId(null);
        });
        return messages;
    }

    @Override
    public String getMessageStatus(UUID authenticatedUser, Long messageId) {
        return "";
    }

    @Override
    public String updateMessageStatus(UUID authenticatedUser, Long messageId, String status) {
        if (!status.equals("READ") && !status.equals("UNREAD"))
            throw new ApiException("Unable to update message status");
        int update = messageQueryRepository.updateMessageStatus(authenticatedUser, messageId, status);
        if (update == 0) throw new ApiException("Unable to update message status");
        return status;
    }
}
