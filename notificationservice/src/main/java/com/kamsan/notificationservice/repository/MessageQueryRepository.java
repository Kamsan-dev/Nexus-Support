package com.kamsan.notificationservice.repository;

import com.kamsan.notificationservice.dto.MessageDTO;
import com.kamsan.notificationservice.dto.SendMessageDTO;
import com.kamsan.notificationservice.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.kamsan.notificationservice.repository.query.MessageQuery.*;
import static com.kamsan.notificationservice.utils.NotificationUtils.randomUUID;

@Service
@RequiredArgsConstructor
public class MessageQueryRepository {

    private final JdbcClient jdbc;

    public Message saveNewMessage(UUID authenticatedUser, SendMessageDTO request, Long conversationId) {
        return jdbc.sql(INSERT_MESSAGE_QUERY)
                   .param("messagePublicId", randomUUID.get())
                   .param("conversationId", conversationId)
                   .param("subject", request.subject())
                   .param("content", request.content())
                   .param("userPublicId", authenticatedUser)
                   .param("toEmail", request.toEmail())
                   .query(Message.class)
                   .single();
    }

    public void createNewMessageStatus(Message message) {
        jdbc.sql(INSERT_MESSAGE_STATUS_QUERY)
            .param("messageId", message.getMessageId())
            .param("senderId", message.getSenderId())
            .param("receiverId", message.getReceiverId())
            .update();
    }

    public List<MessageDTO> findMessagesByUserPublicId(UUID authenticatedUser) {
        return jdbc.sql(SELECT_MESSAGES_QUERY)
                   .param("userPublicId", authenticatedUser)
                   .query(MessageDTO.class)
                   .list();
    }
}
