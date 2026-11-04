import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { delay, pipe, switchMap, tap } from 'rxjs';
import { Pagination } from '../core/model/request.model';
import { PageUsers } from '../core/model/user.model';
import { UserService } from '../service/user.service';
import { setError, setFulfilled, setLoading, withRequestStatus } from './request-status.feature';

interface UserState {
  users: PageUsers[];
  currentPage: number;
  totalPages: number;
  totalUsers: number;
  offset: number;
  numberOfElement: number;
}

const initialTicketState: UserState = {
  users: [],
  currentPage: 0,
  totalPages: 0,
  totalUsers: 0,
  offset: 0,
  numberOfElement: 0,
};

export const UserStore = signalStore(
  { providedIn: 'root' },

  withState(initialTicketState),
  withRequestStatus(),

  withMethods((store, userService = inject(UserService)) => ({
    getUsers: rxMethod<Pagination>(
      pipe(
        tap(() => patchState(store, setLoading())),

        switchMap((pageRequest) =>
          userService.getUsers(pageRequest).pipe(
            tapResponse({
              next: (response) => {
                patchState(store, setFulfilled(), {
                  users: response.data.content,
                  currentPage: response.data.pageable.pageNumber,
                  totalPages: response.data.totalPages,
                  totalUsers: response.data.totalElements,
                  offset: response.data.pageable.offset,
                  numberOfElement: response.data.numberOfElements,
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
  })),
  withComputed((store) => ({
    UserStoreError: computed(() => store.error()),
    pagesTotal: computed(() => store.totalPages()),
  })),
);
