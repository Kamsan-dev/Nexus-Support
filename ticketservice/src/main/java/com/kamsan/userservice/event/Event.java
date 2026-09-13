package com.kamsan.userservice.event;

import com.kamsan.userservice.enumeration.TicketType;
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
    private TicketType eventType;
    private Map<String, ?> data;
}
