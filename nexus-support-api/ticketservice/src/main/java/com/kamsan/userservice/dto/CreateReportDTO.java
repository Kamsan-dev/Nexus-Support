package com.kamsan.userservice.dto;

import com.kamsan.userservice.enumeration.TicketPriority;
import com.kamsan.userservice.enumeration.TicketStatus;
import com.kamsan.userservice.enumeration.TicketType;

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
