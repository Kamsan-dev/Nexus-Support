import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { catchError, delay, exhaustMap, pipe, switchMap, tap } from 'rxjs';
import { UpdateTicketRequest } from '../core/model/request/update-ticket.request';
import { Task, Ticket } from '../core/model/ticket.model';
import { TicketService } from '../service/ticket.service';
import { setError, setFulfilled, setLoading, withRequestStatus } from './request-status.feature';
import { HotToastService } from '@ngxpert/hot-toast';
import { CreateTaskRequest, UpdateTaskRequest } from '../core/model/request/create-task.request';
import { UpdateCommentRequest } from '../core/model/request/update-comment.request';

interface TicketDetailsState {
  ticketUpdated: boolean;
  commentUpdated: boolean;
  taskCreated: boolean;
  ticket: Ticket | null;
}

const state: TicketDetailsState = {
  ticketUpdated: false,
  commentUpdated: false,
  taskCreated: false,
  ticket: null,
};

export const TicketDetailsStore = signalStore(
  { providedIn: 'root' },
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
        delay(1000),
        exhaustMap((request) =>
          ticketService.updateTicket(request).pipe(
            tapResponse({
              next: (response) => {
                const currentTicket = store.ticket();
                if (!currentTicket) {
                  return;
                }
                patchState(
                  store,
                  {
                    ticketUpdated: true,
                    ticket: {
                      ...currentTicket,
                      ticket: response.data,
                    },
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
                patchState(store, setFulfilled(), {
                  ticket: {
                    ...store.ticket()!,
                    ticket: {
                      ...store.ticket()!.ticket,
                      assigneePublicId: request.assigneePublicId,
                    },
                    assignee: assignee!,
                  },
                });
                toastService.success('Ticket assignee updated successfully');
              },
              error: (error: HttpErrorResponse) => {
                patchState(store, setError(error));
              },
            }),
          ),
        ),
      ),
    ),
    createTask: rxMethod<CreateTaskRequest>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          ticketService.createTask(request).pipe(
            tapResponse({
              next: (response) => {
                const currentTicket = store.ticket();
                if (!currentTicket) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  taskCreated: true,
                  ticket: {
                    ...currentTicket,
                    tasks: [response.data, ...currentTicket.tasks],
                  },
                });
                toastService.success(response.message);
              },
              error: (error: HttpErrorResponse) => {
                patchState(store, setError(error));
              },
            }),
          ),
        ),
      ),
    ),
    deleteTask: rxMethod<string>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((taskPublicId) =>
          ticketService.deleteTask(taskPublicId).pipe(
            tapResponse({
              next: (response) => {
                const currentTicket = store.ticket();
                if (!currentTicket) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  ticket: {
                    ...currentTicket,
                    tasks: currentTicket.tasks.filter((task) => task.taskPublicId != taskPublicId),
                  },
                });
                toastService.success(response.message);
              },
              error: (error: HttpErrorResponse) => {
                patchState(store, setError(error));
              },
            }),
          ),
        ),
      ),
    ),
    updateTask: rxMethod<UpdateTaskRequest>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          ticketService.updateTask(request).pipe(
            tapResponse({
              next: (response) => {
                const currentTicket = store.ticket();
                if (!currentTicket) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  ticket: {
                    ...currentTicket,
                    tasks: currentTicket.tasks.map((task) => (task.taskPublicId === response.data.taskPublicId ? response.data : task)),
                  },
                });
                toastService.success(response.message);
              },
              error: (error: HttpErrorResponse) => {
                patchState(store, setError(error));
              },
            }),
          ),
        ),
      ),
    ),
    addComment: rxMethod<{ ticketPublicId: string; comment: string }>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          ticketService.addComment(request.ticketPublicId, request.comment).pipe(
            tapResponse({
              next: (response) => {
                const currentTicket = store.ticket();
                if (!currentTicket) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  ticket: {
                    ...currentTicket,
                    comments: [response.data, ...currentTicket.comments],
                  },
                });
                toastService.success(response.message);
              },
              error: (error: HttpErrorResponse) => {
                patchState(store, setError(error));
              },
            }),
          ),
        ),
      ),
    ),
    editComment: rxMethod<UpdateCommentRequest>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          ticketService.editComment(request).pipe(
            tapResponse({
              next: (response) => {
                const currentTicket = store.ticket();
                if (!currentTicket) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  commentUpdated: true,
                  ticket: {
                    ...currentTicket,
                    comments: currentTicket.comments.map((c) => (c.commentPublicId === request.commentPublicId ? response.data : c)),
                  },
                });
                toastService.success(response.message);
              },
              error: (error: HttpErrorResponse) => {
                patchState(store, setError(error));
              },
            }),
          ),
        ),
      ),
    ),
    deleteComment: rxMethod<string>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((commentPublicId) =>
          ticketService.deleteComment(commentPublicId).pipe(
            tapResponse({
              next: (response) => {
                const currentTicket = store.ticket();
                if (!currentTicket) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  commentUpdated: true,
                  ticket: {
                    ...currentTicket,
                    comments: currentTicket.comments.filter((c) => c.commentPublicId !== commentPublicId),
                  },
                });
                toastService.success(response.message);
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
    resetCommentUpdated(): void {
      patchState(store, { commentUpdated: false });
    },
    resetTaskCreated(): void {
      patchState(store, { taskCreated: false });
    },
  })),
  withComputed((store) => ({
    issuerTicket: computed(() => store.ticket()?.issuerTicket),
    ticketDetail: computed(() => store.ticket()?.ticket),
    currentTicket: computed(() => store.ticket()),
    techSupports: computed(() => store.ticket()?.techSupports),
    tasksList: computed(() => store.ticket()?.tasks),
    commentsList: computed(() => store.ticket()?.comments),
  })),
);
