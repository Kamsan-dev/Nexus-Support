package com.kamsan.ticketservice.event;

import com.kamsan.ticketservice.domain.Notification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class ApiEventListener {

    private final KafkaTemplate<String, Notification> kafkaTemplate;
    private static final String NOTIFICATION_TOPIC = "NOTIFICATION_TOPIC";

    @EventListener
    public void onApiEvent(Event event) {
        var message = MessageBuilder.withPayload(new Notification(event)).setHeader(KafkaHeaders.TOPIC ,NOTIFICATION_TOPIC).build();
        kafkaTemplate.send(message);
    }


}
