import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import {
  patchState,
  signalStore,
  withComputed,
  withMethods,
  withProps,
  withState,
} from '@ngrx/signals';
import { UserService } from '../service/user.service';
import { Profile } from '../core/model/user.model';
import { catchError, EMPTY, pipe, switchMap, tap } from 'rxjs';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { tapResponse } from '@ngrx/operators';

interface AuthState {
  loading: boolean;
  error: HttpErrorResponse | null;
  profile: Profile | undefined;
}

const initialAuthState: AuthState = {
  loading: false,
  error: null,
  profile: undefined,
};

export const AuthStore = signalStore(
  { providedIn: 'root' },

  withState(initialAuthState),

  withMethods((store, userService = inject(UserService)) => ({
    loadProfile: rxMethod<void>(
      pipe(
        tap(() => patchState(store, { loading: true, error: null })),
        switchMap(() =>
          userService.getProfile().pipe(
            tap((response) =>
              patchState(store, {
                loading: false,
                profile: response.data,
              }),
            ),
            catchError((error: HttpErrorResponse) => {
              patchState(store, { error: error });
              return EMPTY;
            }),
          ),
        ),
      ),
    ),
  })),
  withMethods((store) => ({
    hasAnyAuthorities(authorities: string): boolean {
      if (store.profile === undefined) return false;
      let splitAuthorities = store.profile()?.user.authorities.split(',');
      return splitAuthorities?.includes(authorities) ?? false;
    },
  })),
  withComputed((store) => ({
    profile: computed(() => store.profile() ?? undefined),
    profileLoading: computed(() => store.loading()),
    profileError: computed(() => store.error()),
    user: computed(() => store.profile()?.user ?? undefined),
    devices: computed(() => store.profile()?.devices ?? []),
  })),
);
