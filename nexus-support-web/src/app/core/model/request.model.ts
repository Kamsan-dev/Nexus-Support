import { HttpParams } from '@angular/common/http';
import { TicketFilters } from './ticket.model';

export interface Pagination {
  page: number;
  size: number;
  sort?: string[];
}

export interface Pageable {
  pageNumber: number;
  pageSize: number;
  sort: Sort;
  offset: number;
  paged: boolean;
  unpaged: boolean;
}

export interface Sort {
  empty: boolean;
  sorted: boolean;
  unsorted: boolean;
}

export interface Page<T> {
  content: T[];
  pageable: Pageable;
  last: boolean;
  totalElements: number;
  totalPages: number;
  sort: Sort;
  number: number;
  size: number;
  first: boolean;
  numberOfElements: number;
  empty: boolean;
}

export const createPaginationOption = <T extends object>(
  req: Pagination,
  filters?: T,
): HttpParams => {
  let params = new HttpParams();
  params = params.append('page', req.page).append('size', req.size);

  req.sort?.forEach((value) => {
    params = params.append('sort', value);
  });

  Object.entries(filters ?? {}).forEach(([key, value]) => {
    if (value !== null && value !== undefined && value !== '') {
      params = params.set(key, String(value));
    }
  });

  return params;
};
