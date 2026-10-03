package com.kamsan.notificationservice.event;

import com.kamsan.notificationservice.enumeration.EventType;
import lombok.*;

import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private EventType eventType;
    private Map<String, Object> data;
}
