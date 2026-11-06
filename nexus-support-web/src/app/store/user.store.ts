import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { delay, exhaustMap, pipe, switchMap, tap } from 'rxjs';
import { Pagination } from '../core/model/request.model';
import { PageUsers, User } from '../core/model/user.model';
import { UserService } from '../service/user.service';
import { setError, setFulfilled, setLoading, withRequestStatus } from './request-status.feature';
import { ProfileService } from '../service/profile.service';
import { HotToastService } from '@ngxpert/hot-toast';
import { UpdateProfileRequest } from '../core/model/request/update-profile.request';

interface UserState {
  users: PageUsers[];
  currentPage: number;
  totalPages: number;
  totalUsers: number;
  offset: number;
  numberOfElement: number;
  currentUser: User | null;
  currentUserUpdated: boolean;
}

const initialTicketState: UserState = {
  users: [],
  currentPage: 0,
  totalPages: 0,
  totalUsers: 0,
  offset: 0,
  numberOfElement: 0,
  currentUser: null,
  currentUserUpdated: false,
};

export const UserStore = signalStore(
  { providedIn: 'root' },

  withState(initialTicketState),
  withRequestStatus(),

  withMethods((store, userService = inject(UserService), profileService = inject(ProfileService), toastService = inject(HotToastService)) => ({
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
    getUser: rxMethod<string>(
      pipe(
        tap(() => patchState(store, setLoading())),
        switchMap((request) =>
          userService.getUser(request).pipe(
            tapResponse({
              next: (response) => {
                patchState(store, setFulfilled(), {
                  currentUser: response.data,
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
    toggleAccountLocked: rxMethod<string>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          profileService.toggleAccountLocked(request).pipe(
            tapResponse({
              next: (response) => {
                const currentUser = store.currentUser();
                if (!currentUser) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  currentUser: {
                    ...currentUser,
                    isAccountLocked: !currentUser.isAccountLocked,
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
    toggleAccountExpired: rxMethod<string>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          profileService.toggleAccountExpired(request).pipe(
            tapResponse({
              next: (response) => {
                const currentUser = store.currentUser();
                if (!currentUser) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  currentUser: {
                    ...currentUser,
                    isAccountExpired: !currentUser.isAccountExpired,
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
    toggleAccountEnabled: rxMethod<string>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          profileService.toggleAccountEnabled(request).pipe(
            tapResponse({
              next: (response) => {
                const currentUser = store.currentUser();
                if (!currentUser) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  currentUser: {
                    ...currentUser,
                    isAccountEnabled: !currentUser.isAccountEnabled,
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
    updateUserRole: rxMethod<{ userPublicId: string; roleName: string }>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          profileService.updateRoleUser(request.userPublicId, request.roleName).pipe(
            tapResponse({
              next: (response) => {
                const currentUser = store.currentUser();
                if (!currentUser) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  currentUser: {
                    ...currentUser,
                    role: response.data.role,
                    authorities: response.data.authorities,
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
    updateProfile: rxMethod<{ userPublicId: string; request: UpdateProfileRequest }>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          userService.updateUser(request.userPublicId, request.request).pipe(
            tapResponse({
              next: (response) => {
                const currentUser = store.currentUser();
                if (!currentUser) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  currentUserUpdated: true,
                  currentUser: response.data,
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
    resetCurrentUserUpdate(): void {
      patchState(store, { currentUserUpdated: false });
    },
  })),
  withComputed((store) => ({
    UserStoreError: computed(() => store.error()),
    pagesTotal: computed(() => store.totalPages()),
  })),
);
