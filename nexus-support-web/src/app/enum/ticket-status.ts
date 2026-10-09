export enum TicketStatus {
  NEW = 'NEW',
  IN_PROGRESS = 'IN_PROGRESS',
  IN_REVIEW = 'IN_REVIEW',
  COMPLETED = 'COMPLETED',
  IMPEDED = 'IMPEDED',
  CLOSED = 'CLOSED',
  PENDING = 'PENDING',
}

export const TicketStatusLabel: Record<TicketStatus, string> = {
  [TicketStatus.NEW]: 'New',
  [TicketStatus.IN_PROGRESS]: 'In progress',
  [TicketStatus.IN_REVIEW]: 'In review',
  [TicketStatus.COMPLETED]: 'Completed',
  [TicketStatus.IMPEDED]: 'Impeded',
  [TicketStatus.CLOSED]: 'Closed',
  [TicketStatus.PENDING]: 'Pending',
};
