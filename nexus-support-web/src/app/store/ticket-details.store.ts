import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { catchError, delay, exhaustMap, pipe, switchMap, tap } from 'rxjs';
import { UpdateTicketRequest } from '../core/model/request/update-ticket.request';
import { Ticket } from '../core/model/ticket.model';
import { TicketService } from '../service/ticket.service';
import { setError, setFulfilled, setLoading, withRequestStatus } from './request-status.feature';
import { HotToastService } from '@ngxpert/hot-toast';

interface TicketDetailsState {
  ticketUpdated: boolean;
  ticket: Ticket | null;
}

const state: TicketDetailsState = {
  ticketUpdated: false,
  ticket: null,
};

export const TicketDetailsStore = signalStore(
  withState(state),
  withRequestStatus(),
  withMethods((store, ticketService = inject(TicketService), toastService = inject(HotToastService)) => ({
    getTicket: rxMethod<string>(
      pipe(
        tap(() => patchState(store, { ticket: undefined }, setLoading())),
        delay(1000),
        switchMap((ticketPublicId) =>
          ticketService.getTicket(ticketPublicId).pipe(
            tapResponse({
              next: (response) => {
                patchState(store, setFulfilled(), {
                  ticket: response.data,
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
    updateTicket: rxMethod<UpdateTicketRequest>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          ticketService.updateTicket(request).pipe(
            tapResponse({
              next: (response) => {
                patchState(
                  store,
                  {
                    ticketUpdated: true,
                  },
                  setFulfilled(),
                );
              },
              error: (error: HttpErrorResponse) => {
                patchState(store, setError(error));
              },
            }),
          ),
        ),
      ),
    ),
    updateTicketAssignee: rxMethod<{ ticketPublicId: string; assigneePublicId: string }>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          ticketService.updateTicketAssignee(request.assigneePublicId, request.ticketPublicId).pipe(
            tapResponse({
              next: (response) => {
                const assignee = store.ticket()?.techSupports.find((assignee) => assignee.userPublicId === request.assigneePublicId);
                (patchState(store, setFulfilled(), {
                  ticket: {
                    ...store.ticket()!,
                    ticket: {
                      ...store.ticket()!.ticket,
                      assigneePublicId: request.assigneePublicId,
                    },
                    assignee: assignee!,
                  },
                }),
                  toastService.success('Ticket assignee updated successfully'));
              },
              error: (error: HttpErrorResponse) => {
                patchState(store, setError(error));
              },
            }),
          ),
        ),
      ),
    ),
    resetTicketUpdated(): void {
      patchState(store, { ticketUpdated: false });
    },
  })),
  withComputed((store) => ({
    issuerTicket: computed(() => store.ticket()?.issuerTicket),
    ticketDetail: computed(() => store.ticket()?.ticket),
    currentTicket: computed(() => store.ticket()),
    techSupports: computed(() => store.ticket()?.techSupports),
    tasksList: computed(() => store.ticket()?.tasks),
  })),
);
