import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { HotToastService } from '@ngxpert/hot-toast';
import { catchError, delay, EMPTY, exhaustMap, pipe, switchMap, tap } from 'rxjs';
import { Pagination } from '../core/model/request.model';
import { TicketReport } from '../core/model/ticket.model';
import { TicketService } from '../service/ticket.service';
import { setError, setFulfilled, setLoading, withRequestStatus } from './request-status.feature';
import { TicketReportRequest } from '../core/model/request/report-request';
import { tapResponse } from '@ngrx/operators';

interface state {
  report: TicketReport[];
  totalElements: number;
  totalPages: number;
  currentPage: number;
  pageRequest: Pagination;
  reportRequest: TicketReportRequest;
}

const initialAuthState: state = {
  report: [],
  totalElements: 0,
  totalPages: 0,
  currentPage: 0,
  pageRequest: { page: 0, size: 8, sort: [] },
  reportRequest: {
    statuses: [],
    priorities: [],
    types: [],
  },
};

export const ReportStore = signalStore(
  { providedIn: 'root' },

  withState(initialAuthState),
  withRequestStatus(),

  withMethods((store, ticketService = inject(TicketService), toastService = inject(HotToastService)) => ({
    getReport: rxMethod<{ pageRequest: Pagination; filters: TicketReportRequest }>(
      pipe(
        tap(() => patchState(store, setLoading())),
        switchMap((request) =>
          ticketService.getTicketsReport(request.pageRequest, request.filters).pipe(
            tap((response) =>
              patchState(
                store,
                {
                  report: response.data.content,
                  totalElements: response.data.totalElements,
                  totalPages: response.data.totalPages,
                  currentPage: response.data.pageable.pageNumber,

                  pageRequest: request.pageRequest,
                  reportRequest: request.filters,
                },
                setFulfilled(),
              ),
            ),
            catchError((error: HttpErrorResponse) => {
              patchState(store, setError(error));
              return EMPTY;
            }),
          ),
        ),
      ),
    ),
    downloadReport: rxMethod<TicketReportRequest>(
      pipe(
        tap(() => patchState(store)),
        exhaustMap((request) =>
          ticketService.downloadPdfReport(request).pipe(
            tapResponse({
              next: (response: HttpResponse<Blob>) => {
                const blob = response.body!;
                const url = URL.createObjectURL(blob);
                const link = document.createElement('a');
                console.log(response.headers);

                link.href = url;
                link.download = response.headers.get('File-Name')!;

                document.body.appendChild(link);
                link.click();
                link.remove();
              },
              error: (error: HttpErrorResponse) => {
                patchState(store, setError(error));
              },
            }),
          ),
        ),
      ),
    ),
  })),
  withComputed((store) => ({})),
);
