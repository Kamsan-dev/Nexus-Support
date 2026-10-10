package com.kamsan.ticketservice.event;

import com.kamsan.ticketservice.enumeration.EventType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Event {
    private EventType eventType;
    private Map<String, ?> data;
}
