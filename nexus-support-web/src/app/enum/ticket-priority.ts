export enum TicketPriority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
}

export const TicketPriorityLabel: Record<TicketPriority, string> = {
  [TicketPriority.LOW]: 'Low',
  [TicketPriority.MEDIUM]: 'Medium',
  [TicketPriority.HIGH]: 'High',
};
