export enum TicketType {
  BUG = 'BUG',
  DEFECT = 'DEFECT',
  INCIDENT = 'INCIDENT',
  ENHANCEMENT = 'ENHANCEMENT',
  DESIGN = 'DESIGN',
}

export const TicketTypeLabel: Record<TicketType, string> = {
  [TicketType.BUG]: 'Bug',
  [TicketType.DEFECT]: 'Defect',
  [TicketType.INCIDENT]: 'Incident',
  [TicketType.ENHANCEMENT]: 'Enhancement',
  [TicketType.DESIGN]: 'Design',
};
