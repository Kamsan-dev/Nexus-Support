import { TicketPriorityEnum } from '../../../enum/ticket-priority';
import { TicketStatusEnum } from '../../../enum/ticket-status';
import { TicketTypeEnum } from '../../../enum/ticket-type';

export interface UpdateTicketRequest {
  ticketPublicId: string;
  issuerPublicId: string;
  title: string;
  description: string;
  progress: number;
  type: TicketTypeEnum;
  priority: TicketPriorityEnum;
  status: TicketStatusEnum;
  dueDate: string;
}
