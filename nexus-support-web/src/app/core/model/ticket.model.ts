import { TicketPriority } from '../../enum/ticket-priority';
import { TicketStatusEnum } from '../../enum/ticket-status';
import { TicketTypeEnum } from '../../enum/ticket-type';

export interface PageTicket {
  createdAt: string;
  updatedAt: string;
  ticketPublicId: string;
  title: string;
  description: string;
  progress: number;
  status: TicketStatusEnum;
  priority: TicketPriority;
  type: TicketTypeEnum;
  dueDate: string;
  fileCount: number;
  commentCount: number;
}

//#region Pagination

export interface TicketFilters {
  filter?: string;
  type?: TicketTypeEnum;
  status?: TicketStatusEnum;
}

//#endregion
