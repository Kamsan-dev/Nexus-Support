import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { catchError, EMPTY, pipe, switchMap, tap } from 'rxjs';
import { Profile } from '../core/model/user.model';
import { UserService } from '../service/user.service';
import { setError, setFulfilled, setLoading, withRequestStatus } from './request-status.feature';
import { RoleEnum } from '../enum/role';

interface AuthState {
  profile: Profile | undefined;
}

const initialAuthState: AuthState = {
  profile: undefined,
};

export const AuthStore = signalStore(
  { providedIn: 'root' },

  withState(initialAuthState),
  withRequestStatus(),

  withMethods((store, userService = inject(UserService)) => ({
    loadProfile: rxMethod<void>(
      pipe(
        tap(() => patchState(store, setLoading())),
        switchMap(() =>
          userService.getProfile().pipe(
            tap((response) =>
              patchState(
                store,
                {
                  profile: response.data,
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
    hasAnyAuthorities(authorities: string): boolean {
      if (store.profile === undefined) return false;
      let splitAuthorities = store.profile()?.user.authorities.split(',');
      return splitAuthorities?.includes(authorities) ?? false;
    },
    hasAnyRole(roles: RoleEnum[]): boolean {
      const profile = store.profile();
      if (!profile) {
        return false;
      }
      return roles.includes(profile.user.role);
    },
  })),
  withComputed((store) => ({
    user: computed(() => store.profile()?.user ?? undefined),
    devices: computed(() => store.profile()?.devices ?? []),
    roleConnectedUser: computed(() => store.profile()?.user.role),
  })),
);
