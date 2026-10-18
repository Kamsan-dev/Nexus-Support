package com.kamsan.ticketservice.enumeration;
import lombok.Getter;

@Getter
public enum TicketStatus {
    NEW("NEW"),
    IN_PROGRESS("IN_PROGRESS"),
    IN_REVIEW("IN_REVIEW"),
    COMPLETED("COMPLETED"),
    IMPEDED("IMPEDED"),
    CLOSED("CLOSED"),
    PENDING("PENDING");

    private final String value;

    TicketStatus(String value) {
        this.value = value;
    }
}