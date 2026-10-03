package com.kamsan.notificationservice.event.listener;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kamsan.notificationservice.domain.Data;
import com.kamsan.notificationservice.domain.Notification;
import com.kamsan.notificationservice.dto.SendCommentEmailDTO;
import com.kamsan.notificationservice.dto.SendFilesEmailDTO;
import com.kamsan.notificationservice.dto.SendTicketEmailDTO;
import com.kamsan.notificationservice.dto.SendTokenEmailDTO;
import com.kamsan.notificationservice.service.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationListener {

    private static final String NOTIFICATION_TOPIC = "NOTIFICATION_TOPIC";
    private final EmailService emailService;

    @KafkaListener(topics = NOTIFICATION_TOPIC)
    public void handleNotification(Notification notification) {
        log.info("Received notification : {}", notification.toString());
        var mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        var data = mapper.convertValue(notification.getPayload().getData(), Data.class);
        switch (notification.getPayload().getEventType()) {
            case RESET_PASSWORD -> emailService.sendPasswordResetHtmlEmail(
                    new SendTokenEmailDTO(
                            data.getName(),
                            data.getEmail(),
                            data.getToken()));
            case USER_CREATED -> emailService.sendNewAccountHtmlEmail(
                    new SendTokenEmailDTO(
                            data.getName(),
                            data.getEmail(),
                            data.getToken()));
            case TICKET_CREATED -> emailService.sendNewTicketHtmlEmail(
                    new SendTicketEmailDTO(
                            data.getName(),
                            data.getEmail(),
                            data.getTicketTitle(),
                            data.getTicketNumber(),
                            data.getPriority()));
            case FILE_UPLOADED -> emailService.sendNewFilesHtmlEmail(
                    new SendFilesEmailDTO(
                            data.getName(),
                            data.getEmail(),
                            data.getFiles(),
                            data.getTicketTitle(),
                            data.getTicketNumber(),
                            data.getPriority(),
                            data.getDate()));
            case COMMENT_CREATED -> emailService.sendNewCommentHtmlEmail(
                    new SendCommentEmailDTO(
                            data.getName(),
                            data.getEmail(),
                            data.getComment(),
                            data.getTicketTitle(),
                            data.getTicketNumber(),
                            data.getPriority(),
                            data.getDate()));
        }
    }
}
