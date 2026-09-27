package com.kamsan.notificationservice.repository;

import com.kamsan.notificationservice.dto.MessageDTO;
import com.kamsan.notificationservice.dto.SendMessageDTO;
import com.kamsan.notificationservice.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.kamsan.notificationservice.repository.query.MessageQuery.*;
import static com.kamsan.notificationservice.utils.NotificationUtils.randomUUID;

@Service
@RequiredArgsConstructor
public class MessageQueryRepository {

    private final JdbcClient jdbc;

    public Message saveNewMessage(UUID authenticatedUser, SendMessageDTO request, UUID conversationId) {
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

    public List<MessageDTO> findMessagesByConversationId(UUID authenticatedUser, UUID conversationId) {
        return jdbc.sql(SELECT_MESSAGES_BY_CONVERSATION_ID_QUERY)
                   .param("userPublicId", authenticatedUser)
                   .param("conversationId", conversationId)
                   .query(MessageDTO.class)
                   .list();
    }

    public String findMessageStatus(UUID authenticatedUser, Long messageId) {
        return jdbc.sql(SELECT_MESSAGE_STATUS_QUERY)
                   .param("userPublicId", authenticatedUser)
                   .param("messageId", messageId)
                   .query(String.class)
                   .single();
    }

    public int updateMessageStatus(UUID authenticatedUser, Long messageId, String messageStatus) {
        return jdbc.sql(UPDATE_MESSAGE_STATUS_QUERY)
                   .param("userPublicId", authenticatedUser)
                   .param("messageId", messageId)
                   .param("messageStatus", messageStatus)
                   .update();
    }

    public Optional<UUID> findConversationId(UUID authenticatedUser, String toEmail) {
        return jdbc.sql(SELECT_CONVERSATION_ID_QUERY)
                   .param("authenticatedUserPublicId", authenticatedUser)
                   .param("receiverEmail", toEmail)
                   .query(UUID.class)
                   .optional();
    }
}

