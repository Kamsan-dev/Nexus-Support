import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { catchError, delay, EMPTY, exhaustMap, pipe, switchMap, tap } from 'rxjs';
import { Profile } from '../core/model/user.model';
import { UserService } from '../service/user.service';
import { clearError, setError, setFulfilled, setLoading, withRequestStatus } from './request-status.feature';
import { RoleEnum } from '../enum/role';
import { UpdateProfileRequest } from '../core/model/request/update-profile.request';
import { ProfileService } from '../service/profile.service';
import { tapResponse } from '@ngrx/operators';
import { HotToastService } from '@ngxpert/hot-toast';
import { UpdatePasswordRequest } from '../core/model/request/update-password.request';

interface AuthState {
  profile: Profile | undefined;
  profileUpdated: boolean;
}

const initialAuthState: AuthState = {
  profile: undefined,
  profileUpdated: false,
};

export const AuthStore = signalStore(
  { providedIn: 'root' },

  withState(initialAuthState),
  withRequestStatus(),

  withMethods((store, userService = inject(UserService), profileService = inject(ProfileService), toastService = inject(HotToastService)) => ({
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
    updateProfile: rxMethod<{ userPublicId: string; request: UpdateProfileRequest }>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          userService.updateUser(request.userPublicId, request.request).pipe(
            delay(1500),
            tapResponse({
              next: (response) => {
                const profile = store.profile();
                if (!profile) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  profileUpdated: true,
                  profile: {
                    ...profile,
                    user: response.data,
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
    updatePassword: rxMethod<UpdatePasswordRequest>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          profileService.updatePasswordUser(request).pipe(
            tapResponse({
              next: (response) => {
                const profile = store.profile();
                if (!profile) {
                  return;
                }
                patchState(store, setFulfilled());
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
    toggleAccountLocked: rxMethod<string>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((request) =>
          profileService.toggleAccountLocked(request).pipe(
            tapResponse({
              next: (response) => {
                const profile = store.profile();
                if (!profile) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  profile: {
                    ...profile,
                    user: {
                      ...profile.user,
                      isAccountLocked: !profile.user.isAccountLocked,
                    },
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
                const profile = store.profile();
                if (!profile) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  profile: {
                    ...profile,
                    user: {
                      ...profile.user,
                      isAccountExpired: !profile.user.isAccountExpired,
                    },
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
                const profile = store.profile();
                if (!profile) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  profile: {
                    ...profile,
                    user: {
                      ...profile.user,
                      isAccountEnabled: !profile.user.isAccountEnabled,
                    },
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
                const profile = store.profile();
                if (!profile) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  profile: {
                    ...profile,
                    user: {
                      ...profile.user,
                      role: response.data.role,
                      authorities: response.data.authorities,
                    },
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
    enableMfa: rxMethod<void>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap(() =>
          profileService.enableMfa().pipe(
            tapResponse({
              next: (response) => {
                const profile = store.profile();
                if (!profile) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  profile: {
                    ...profile,
                    user: {
                      ...profile.user,
                      isUsingMfa: true,
                      qrCodeImageUri: response.data.qrCodeImageUri,
                    },
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
    disableMfa: rxMethod<void>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap(() =>
          profileService.disableMfa().pipe(
            tapResponse({
              next: (response) => {
                const profile = store.profile();
                if (!profile) {
                  return;
                }
                patchState(store, setFulfilled(), {
                  profile: {
                    ...profile,
                    user: {
                      ...profile.user,
                      isUsingMfa: false,
                      qrCodeImageUri: null,
                    },
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
    resetProfileUpdated(): void {
      patchState(store, { profileUpdated: false });
    },
    hasAnyAuthorities(authorities: string[] | string): boolean {
      if (store.profile === undefined) return false;
      let splitAuthorities = store.profile()?.user.authorities.split(',');
      if (!Array.isArray(authorities)) {
        authorities = [authorities];
      }
      return authorities.some((authority) => splitAuthorities?.includes(authority)) ?? false;
    },
    hasAnyRole(roles: RoleEnum[]): boolean {
      const profile = store.profile();
      if (!profile) {
        return false;
      }
      return roles.includes(profile.user.role);
    },
    resetProfileError(): void {
      patchState(store, clearError());
    },
  })),
  withComputed((store) => ({
    user: computed(() => store.profile()?.user ?? undefined),
    devices: computed(() => store.profile()?.devices ?? []),
    roleConnectedUser: computed(() => store.profile()?.user.role),
  })),
);

// ['aaaaaa'] | aaaaa
