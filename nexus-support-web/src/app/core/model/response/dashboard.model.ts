import { TicketStatusEnum } from '../../../enum/ticket-status';
import { TicketTypeEnum } from '../../../enum/ticket-type';
import { TicketDetails } from '../ticket.model';

export interface TicketDashboardStats {
  ticketCount: number;
  ticketsByStatus: Array<TicketByStatus>;
  ticketsByType: Array<TicketByType>;
  weeklyTrends: Array<WeeklyTrendDTO>;
  oldestOpenTickets: Array<TicketDetails>;
}

export interface TicketByType {
  ticketCount: number;
  type: TicketTypeEnum;
}

export interface TicketByStatus {
  ticketCount: number;
  status: TicketStatusEnum;
}

export interface WeeklyTrendDTO {
  weekStart: string;
  newCount: number;
  completedCount: number;
}
