package com.kamsan.ticketservice.dto;

import com.kamsan.ticketservice.enumeration.TicketPriority;
import com.kamsan.ticketservice.enumeration.TicketStatus;
import com.kamsan.ticketservice.enumeration.TicketType;

import java.time.LocalDate;
import java.util.List;

public record CreateReportDTO(
        String filter,
        LocalDate fromDate,
        LocalDate toDate,
        List<TicketStatus> statuses,
        List<TicketPriority> priorities,
        List<TicketType> types
) {
}
