import { TicketStatusEnum } from '../../../enum/ticket-status';
import { Pagination } from '../request.model';
import { TicketFilters } from '../ticket.model';

export interface QueryRequest<T extends object> {
  pageRequest: Pagination;
  filters?: T;
}

export const defaultTicketQueryFilter: QueryRequest<TicketFilters> = {
  pageRequest: { page: 0, size: 10, sort: [] },
  filters: {},
};
