import { TicketPriorityEnum } from '../../../enum/ticket-priority';
import { TicketTypeEnum } from '../../../enum/ticket-type';

export interface CreateTicketRequest {
  title: string;
  description: string;
  priority: TicketPriorityEnum;
  type: TicketTypeEnum;
}
