package com.kamsan.ticketservice.dto;

import com.kamsan.ticketservice.enumeration.TicketPriority;
import com.kamsan.ticketservice.enumeration.TicketStatus;
import com.kamsan.ticketservice.enumeration.TicketType;

import java.time.OffsetDateTime;
import java.util.List;

public record CreateReportDTO(
        String filter,
        OffsetDateTime fromDate,
        OffsetDateTime toDate,
        List<TicketStatus> statuses,
        List<TicketPriority> priorities,
        List<TicketType> types
) {
}
