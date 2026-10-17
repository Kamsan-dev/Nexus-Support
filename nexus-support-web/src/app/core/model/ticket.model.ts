import { RoleEnum } from '../../enum/role';
import { TicketPriorityEnum } from '../../enum/ticket-priority';
import { TicketStatusEnum } from '../../enum/ticket-status';
import { TicketTypeEnum } from '../../enum/ticket-type';
import { User } from './user.model';

export interface PageTicket {
  createdAt: string;
  updatedAt: string;
  ticketPublicId: string;
  title: string;
  description: string;
  progress: number;
  status: TicketStatusEnum;
  priority: TicketPriorityEnum;
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

export interface Comment {
  createdAt: Date;
  updatedAt: Date;
  commentPublicId: string;
  userPublicId: string;
  ticketPublcId: string;
  comment: string;
  isEdited: boolean;
  firstName: string;
  lastName: string;
  imageUrl: string;
}

export interface Attachment {
  ticketPublicId: string;
  name: string;
  size: number;
  formattedSize: string;
  extension: string;
  uri: string;
}

export interface Task {
  taskPublicId: string;
  ticketPublicId: string;
  assigneePublicId: string;
  name: string;
  description: string;
  dueDate: Date;
  status: TicketStatusEnum;
  assigneeFirstName: string;
  assigneeLastName: string;
  assigneeImageUrl: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface TicketUser {
  userPublicId: string;
  email: string;
  firstName: string;
  lastName: string;
  imageUrl: string;
  role: RoleEnum;
}

export interface TicketDetails {
  createdAt: Date;
  updatedAt: Date;
  ticketPublicId: string;
  issuerPublicId: string;
  assigneePublicId: string;
  title: string;
  description: string;
  progress: number;
  status: TicketStatusEnum;
  priority: TicketPriorityEnum;
  type: TicketTypeEnum;
  dueDate: Date;
}

export interface Ticket {
  ticket: TicketDetails;
  comments: Comment[];
  attachments: Attachment[];
  tasks: Task[];
  techSupports: TicketUser[];
  assignee: TicketUser | null;
  issuerTicket: User;
}
