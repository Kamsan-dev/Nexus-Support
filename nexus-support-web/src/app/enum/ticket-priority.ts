export enum TicketPriorityEnum {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
}

export const TicketPriorityLabel: Record<TicketPriorityEnum, string> = {
  [TicketPriorityEnum.LOW]: 'Low',
  [TicketPriorityEnum.MEDIUM]: 'Medium',
  [TicketPriorityEnum.HIGH]: 'High',
};

export const TicketPriorityColors: Record<TicketPriorityEnum, string> = {
  [TicketPriorityEnum.LOW]: 'bg-green-100 text-green-700',
  [TicketPriorityEnum.MEDIUM]: 'bg-amber-100 text-amber-700',
  [TicketPriorityEnum.HIGH]: 'bg-red-100 text-red-700',
};
