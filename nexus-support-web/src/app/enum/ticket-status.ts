export enum TicketStatusEnum {
  NEW = 'NEW',
  IN_PROGRESS = 'IN_PROGRESS',
  IN_REVIEW = 'IN_REVIEW',
  COMPLETED = 'COMPLETED',
  IMPEDED = 'IMPEDED',
  CLOSED = 'CLOSED',
  PENDING = 'PENDING',
}

export const TicketStatusLabel: Record<TicketStatusEnum, string> = {
  [TicketStatusEnum.NEW]: 'New',
  [TicketStatusEnum.IN_PROGRESS]: 'In progress',
  [TicketStatusEnum.IN_REVIEW]: 'In review',
  [TicketStatusEnum.COMPLETED]: 'Completed',
  [TicketStatusEnum.IMPEDED]: 'Impeded',
  [TicketStatusEnum.CLOSED]: 'Closed',
  [TicketStatusEnum.PENDING]: 'Pending',
};

export const TicketStatusColor: Record<TicketStatusEnum, string> = {
  [TicketStatusEnum.NEW]: 'bg-blue-100 text-blue-800',
  [TicketStatusEnum.IN_PROGRESS]: 'bg-yellow-100 text-yellow-800',
  [TicketStatusEnum.IN_REVIEW]: 'bg-purple-100 text-purple-800',
  [TicketStatusEnum.COMPLETED]: 'bg-green-100 text-green-800',
  [TicketStatusEnum.IMPEDED]: 'bg-red-100 text-red-800',
  [TicketStatusEnum.CLOSED]: 'bg-gray-100 text-gray-800',
  [TicketStatusEnum.PENDING]: 'bg-orange-100 text-orange-800',
};
