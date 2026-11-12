package com.kamsan.notificationservice.service.implementation;

import com.kamsan.notificationservice.dto.*;
import com.kamsan.notificationservice.model.Message;
import com.kamsan.notificationservice.repository.MessageQueryRepository;
import com.kamsan.notificationservice.service.MessageService;
import com.kamsan.notificationservice.sharedkernel.exception.ApiException;
import com.kamsan.notificationservice.utils.NotificationUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class MessageServiceImpl implements MessageService {
    private final MessageQueryRepository messageQueryRepository;

    @Override
    @Transactional(readOnly = true)
    public ConversationOverviewsDTO getMessages(UUID authenticatedUser) {
        List<ConversationOverviewDTO> conversationOverviews = messageQueryRepository.findConversationOverviews(
                authenticatedUser);
        long unreadMessagesCount = messageQueryRepository.findUnreadMessagesCount(authenticatedUser);

        return new ConversationOverviewsDTO(conversationOverviews, unreadMessagesCount);

    }

    @Override
    @Transactional
    public ConversationOverviewDTO sendMessage(UUID authenticatedUser, SendMessageDTO sendMessageDTO) {
        UUID conversationId = messageQueryRepository.findConversationId(authenticatedUser,
                sendMessageDTO.receiverPublicId()).orElse(NotificationUtils.randomUUID.get());

        Message message = messageQueryRepository.saveNewMessage(authenticatedUser, sendMessageDTO, conversationId);
        messageQueryRepository.createNewMessageStatus(message);
        return messageQueryRepository.findConversationOverviewByConversationId(
                authenticatedUser,
                conversationId,
                sendMessageDTO.receiverPublicId());
    }


    @Override
    @Transactional
    public ConversationMessagesDTO getConversation(UUID authenticatedUser, UUID conversationId) {
        List<MessageDTO> messages = messageQueryRepository.findMessagesByConversationId(
                authenticatedUser,
                conversationId);

        Set<Long> updatedMessageIds = Set.copyOf(updateMessageStatus(authenticatedUser, conversationId)
        );

        messages.forEach(m -> {
            if (updatedMessageIds.contains(m.getMessageId())){
                m.setStatus("READ");
            }
        });
        UUID correspondentPublicId = messages.stream()
                                     .findFirst()
                                     .map(message -> message.getSenderPublicId().equals(authenticatedUser)
                                             ? message.getReceiverPublicId()
                                             : message.getSenderPublicId())
                .orElseThrow(() -> new ApiException("An error occurred when retrieving the conversation. " +
                        "Please try again."));



        return new ConversationMessagesDTO(messages, updatedMessageIds.size(), correspondentPublicId);
    }

    @Override
    public List<RecipientDTO> getAllRecipients() {
        return messageQueryRepository.findRecipients();
    }

    private List<Long> updateMessageStatus(UUID authenticatedUser, UUID conversationId) {
        return messageQueryRepository.updateMessageStatus(authenticatedUser, conversationId);
    }
}
