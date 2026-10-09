import { TicketStatus } from '../../enum/ticket-status';
import { TicketType } from '../../enum/ticket-type';
import { Pageable } from './request.model';

export interface PageTicket {
  createdAt: Date;
  updatedAt: Date;
  ticketPublicId: string;
  title: string;
  description: string;
  progress: number;
  status: string;
  priority: string;
  type: string;
  dueDate: Date;
  fileCount: number;
  commentCount: number;
}

//#region Pagination

export interface PageTicketRequest {
  page: Pageable;
  status: TicketStatus;
  type: TicketType;
  filter: string;
}

export interface TicketFilters {
  filter?: string;
  type?: TicketType;
  status?: TicketStatus;
}

//#endregion
