package com.kamsan.ticketservice.dto;

public record TicketReportQueryResult(
        TicketReportDTO ticket,
        long totalElements
) {}