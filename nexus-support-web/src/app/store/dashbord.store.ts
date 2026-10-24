import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { delay, pipe, switchMap, tap } from 'rxjs';
import { TicketDashboardStats } from '../core/model/response/dashboard.model';
import { TicketService } from '../service/ticket.service';
import { setError, setFulfilled, setLoading, withRequestStatus } from './request-status.feature';
import { TicketStatusEnum } from '../enum/ticket-status';

export const DashboardStore = signalStore(
  { providedIn: 'root' },

  withState(<{ stats: TicketDashboardStats | null; statsRetrieved: boolean }>{
    stats: null,
    statsRetrieved: false,
  }),
  withRequestStatus(),

  withMethods((store, ticketService = inject(TicketService)) => ({
    loadStats: rxMethod<void>(
      pipe(
        tap(() => patchState(store, setLoading())),
        delay(1000),
        switchMap(() =>
          ticketService.getDashboardStats().pipe(
            tapResponse({
              next: (response) => {
                patchState(store, setFulfilled(), {
                  stats: response.data,
                  statsRetrieved: true,
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
    resetStatsRetrieved(): void {
      patchState(store, { statsRetrieved: false });
    },
  })),
  withComputed((store) => ({
    ticketCompletedCount: computed(() => {
      return store.stats()?.ticketsByStatus.find((s) => s.status === TicketStatusEnum.COMPLETED)?.ticketCount;
    }),
    ticketInProgressCount: computed(() => {
      return store.stats()?.ticketsByStatus.find((s) => s.status === TicketStatusEnum.IN_PROGRESS)?.ticketCount;
    }),
    ticketImpededCount: computed(() => {
      return store.stats()?.ticketsByStatus.find((s) => s.status === TicketStatusEnum.IMPEDED)?.ticketCount;
    }),
  })),
);
