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
