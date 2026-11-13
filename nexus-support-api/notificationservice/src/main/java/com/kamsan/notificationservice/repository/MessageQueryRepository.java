package com.kamsan.notificationservice.repository;

import com.kamsan.notificationservice.dto.MessageDTO;
import com.kamsan.notificationservice.dto.RecipientDTO;
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
                   .param("content", request.content())
                   .param("userPublicId", authenticatedUser)
                   .param("receiverPublicId", request.receiverPublicId())
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

    public List<MessageDTO> findConversationOverviews(UUID authenticatedUser) {
        return jdbc.sql(SELECT_CONVERSATION_OVERVIEW_QUERY)
                   .param("userPublicId", authenticatedUser)
                   .query(MessageDTO.class)
                   .list();
    }

    public MessageDTO findConversationOverviewByConversationId(UUID authenticatedUser, UUID conversationId, UUID receiverPublicId) {
        return jdbc.sql(SELECT_CONVERSATION_OVERVIEW_BY_CONVERSATION_ID_QUERY)
                   .param("authenticatedUserPublicId", authenticatedUser)
                   .param("conversationId", conversationId)
                   .param("receiverPublicId", receiverPublicId)
                   .query(MessageDTO.class)
                   .single();
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

    public List<Long> updateMessageStatus(UUID authenticatedUser, UUID conversationId) {
        return jdbc.sql(UPDATE_MESSAGE_STATUS_QUERY)
                   .param("userPublicId", authenticatedUser)
                   .param("conversationId", conversationId)
                   .query(Long.class)
                   .list();
    }

    public Optional<UUID> findConversationId(UUID authenticatedUser, UUID receiverPublicId) {
        return jdbc.sql(SELECT_CONVERSATION_ID_QUERY)
                   .param("authenticatedUserPublicId", authenticatedUser)
                   .param("receiverPublicId", receiverPublicId)
                   .query(UUID.class)
                   .optional();
    }

    public long findUnreadMessagesCount(UUID authenticatedUser){
            return jdbc.sql(SELECT_UNREAD_MESSAGE_COUNT_QUERY)
                     .param("userPublicId", authenticatedUser)
                     .query(Long.class)
                     .single();
    }

    public List<RecipientDTO> findRecipients(UUID authenticatedUser) {
        return jdbc.sql(SELECT_RECIPIENTS_QUERY)
                   .param("userPublicId", authenticatedUser)
                   .query(RecipientDTO.class)
                   .list();
    }
}

