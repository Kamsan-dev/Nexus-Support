import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { delay, exhaustMap, pipe, switchMap, tap } from 'rxjs';
import { defaultTicketQueryFilter, QueryRequest } from '../core/model/request/query-request';
import { PageTicket, TicketFilters } from '../core/model/ticket.model';
import { TicketService } from '../service/ticket.service';
import { setError, setFulfilled, setLoading, withRequestStatus } from './request-status.feature';

interface TicketState {
  tickets: PageTicket[];
  currentPage: number;
  query: QueryRequest<TicketFilters>;
  totalPages: number;
  totalTickets: number;
  ticketCreated: boolean;
}

const initialTicketState: TicketState = {
  tickets: [],
  currentPage: 0,
  query: defaultTicketQueryFilter,
  totalPages: 0,
  totalTickets: 0,
  ticketCreated: false,
};

export const TicketStore = signalStore(
  { providedIn: 'root' },

  withState(initialTicketState),
  withRequestStatus(),

  withMethods((store, ticketService = inject(TicketService)) => ({
    getTickets: rxMethod<QueryRequest<TicketFilters>>(
      pipe(
        tap(() => patchState(store, setLoading())),
        delay(0),

        switchMap(({ pageRequest, filters = {} }) =>
          ticketService.getTickets(pageRequest, filters).pipe(
            tapResponse({
              next: (response) => {
                patchState(store, setFulfilled(), {
                  tickets: response.data.content,
                  query: { pageRequest, filters },
                  currentPage: response.data.pageable.pageNumber,
                  totalPages: response.data.totalPages,
                  totalTickets: response.data.totalElements,
                });
              },
              error: (error: HttpErrorResponse) => {
                patchState(store, setError(error));
              },
            }),
          ),
        ),
      ),
    ),
    createTicket: rxMethod<FormData>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((formData) =>
          ticketService.createTicket(formData).pipe(
            tapResponse({
              next: (response) => {
                patchState(store, setFulfilled(), {
                  ticketCreated: true,
                  tickets: [response.data, ...store.tickets()],
                  totalTickets: store.totalTickets() + 1,
                });
              },
              error: (error: HttpErrorResponse) => {
                patchState(store, setError(error));
              },
            }),
          ),
        ),
      ),
    ),
    resetTicketCreated(): void {
      patchState(store, { ticketCreated: false });
    },
  })),
  withComputed((store) => ({
    statusQuery: computed(() => store.query()?.filters?.status),
    typeQuery: computed(() => store.query()?.filters?.type),
    currentQuery: computed(() => store.query()),
    searchTermQuery: computed(() => store.query()?.filters?.filter),
    ticketStoreError: computed(() => store.error()),
    pagesTotal: computed(() => store.totalPages()),
  })),
);
