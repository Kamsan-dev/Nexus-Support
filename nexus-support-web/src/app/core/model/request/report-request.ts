import { TicketPriorityEnum } from '../../../enum/ticket-priority';
import { TicketStatusEnum } from '../../../enum/ticket-status';
import { TicketTypeEnum } from '../../../enum/ticket-type';

export interface TicketReportRequest {
  filter?: string;
  fromDate?: string;
  toDate?: string;
  statuses: Array<TicketStatusEnum>;
  priorities: Array<TicketPriorityEnum>;
  types: Array<TicketTypeEnum>;
}
