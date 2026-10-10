export enum TicketTypeEnum {
  BUG = 'BUG',
  DEFECT = 'DEFECT',
  INCIDENT = 'INCIDENT',
  ENHANCEMENT = 'ENHANCEMENT',
  DESIGN = 'DESIGN',
}

export const TicketTypeLabel: Record<TicketTypeEnum, string> = {
  [TicketTypeEnum.BUG]: 'Bug',
  [TicketTypeEnum.DEFECT]: 'Defect',
  [TicketTypeEnum.INCIDENT]: 'Incident',
  [TicketTypeEnum.ENHANCEMENT]: 'Enhancement',
  [TicketTypeEnum.DESIGN]: 'Design',
};

export const TicketTypeColor: Record<TicketTypeEnum, string> = {
  [TicketTypeEnum.BUG]: 'bg-red-100 text-red-800',
  [TicketTypeEnum.DEFECT]: 'bg-rose-100 text-rose-800',
  [TicketTypeEnum.INCIDENT]: 'bg-orange-100 text-orange-800',
  [TicketTypeEnum.ENHANCEMENT]: 'bg-emerald-100 text-emerald-800',
  [TicketTypeEnum.DESIGN]: 'bg-violet-100 text-violet-800',
};
