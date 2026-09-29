package com.kamsan.userservice.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.kamsan.userservice.sharedkernel.exception.ApiException;

import java.util.Arrays;

public enum TicketStatus {
    NEW("NEW"),
    IN_PROGRESS("IN PROGRESS"),
    IN_REVIEW("IN REVIEW"),
    COMPLETED("COMPLETED"),
    IMPEDED("IMPEDED"),
    CLOSED("CLOSED"),
    PENDING("PENDING");

    private final String value;

    TicketStatus(String value) {
        this.value = value;
    }

    @JsonCreator
    public static TicketStatus fromValue(String value) {
        return Arrays.stream(values())
                     .filter(status -> status.value.equals(value))
                     .findFirst()
                     .orElseThrow(() -> new ApiException(
                             "Invalid ticket status: " + value
                     ));
    }

    @JsonValue
    public String value() {
        return value;
    }
}