package com.kamsan.ticketservice.enumeration;

import lombok.Getter;

@Getter
public enum TicketPriority {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH");

    private final String value;

    TicketPriority(String value) {
        this.value = value;
    }
    
}
