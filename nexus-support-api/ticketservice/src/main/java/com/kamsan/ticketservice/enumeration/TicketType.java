package com.kamsan.ticketservice.enumeration;
import lombok.Getter;

@Getter
public enum TicketType {
    BUG("BUG"),
    DEFECT("DEFECT"),
    INCIDENT("INCIDENT"),
    ENHANCEMENT("ENHANCEMENT"),
    DESIGN("DESIGN");

    private final String value;

    TicketType(String value) {
        this.value = value;
    }
}
