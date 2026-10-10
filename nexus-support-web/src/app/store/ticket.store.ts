import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { delay, pipe, switchMap, tap } from 'rxjs';
import { defaultTicketQueryFilter, QueryRequest } from '../core/model/request/query-request';
import { PageTicket, TicketFilters } from '../core/model/ticket.model';
import { TicketService } from '../service/ticket.service';

interface TicketState {
  loading: boolean;
  error: HttpErrorResponse | null;
  tickets: PageTicket[];
  currentPage: number;
  query: QueryRequest<TicketFilters>;
}

const initialTicketState: TicketState = {
  loading: false,
  error: null,
  tickets: [],
  currentPage: 0,
  query: defaultTicketQueryFilter,
};

export const TicketStore = signalStore(
  { providedIn: 'root' },

  withState(initialTicketState),

  withMethods((store, ticketService = inject(TicketService)) => ({
    getTickets: rxMethod<QueryRequest<TicketFilters>>(
      pipe(
        tap(() => patchState(store, { loading: true, error: null })),
        delay(0),

        switchMap(({ pageRequest, filters = {} }) =>
          ticketService.getTickets(pageRequest, filters).pipe(
            tapResponse({
              next: (response) => {
                patchState(store, {
                  loading: false,
                  tickets: response.data.content,
                  query: { pageRequest, filters },
                  currentPage: response.data.pageable.pageNumber,
                });
              },
              error: (error: HttpErrorResponse) => {
                patchState(store, {
                  loading: false,
                  error: error.error.detail,
                });
              },
            }),
          ),
        ),
      ),
    ),
    setCurrentPage(currentPage: number): void {
      patchState(store, (state) => ({ currentPage }));
    },
  })),
  withComputed((store) => ({
    countTickets: computed(() => store.tickets().length),
    statusQuery: computed(() => store.query()?.filters?.status),
    typeQuery: computed(() => store.query()?.filters?.type),
    currentQuery: computed(() => store.query),
    searchTermQuery: computed(() => store.query()?.filters?.filter),
  })),
);
