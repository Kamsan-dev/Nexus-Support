import { TicketStatusEnum } from '../../../enum/ticket-status';

export interface CreateTaskRequest {
  ticketPublicId: string;
  name: string;
  description: string;
  status: TicketStatusEnum;
}

export interface UpdateTaskRequest {
  taskPublicId: string;
  assigneePublicId: string;
  name: string;
  description: string;
  status: TicketStatusEnum;
}
