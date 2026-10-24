package com.kamsan.ticketservice.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class TicketDashboardStatsDTO {
    private long ticketCount;
    private List<TicketByStatusDTO> ticketsByStatus;
    private List<TicketByTypeDTO> ticketsByType;
    private List<WeeklyTrendDTO> weeklyTrends;
}
