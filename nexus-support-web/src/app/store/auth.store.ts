import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { signalStore, withComputed, withProps } from '@ngrx/signals';
import { UserService } from '../service/user.service';
// import { pipe, switchMap, tap } from 'rxjs';
// import { rxMethod } from '@ngrx/signals/rxjs-interop';
// import { tapResponse } from '@ngrx/operators';

export const AuthStore = signalStore(
  { providedIn: 'root' },

  withProps(() => {
    const userService = inject(UserService);

    return {
      profileResource: rxResource({
        stream: () => userService.getProfile(),
      }),
    };
  }),

  withComputed(({ profileResource }) => ({
    profile: computed(() => profileResource.value()?.data ?? null),
    profileLoading: computed(() => profileResource.isLoading()),
    profileError: computed(() => profileResource.error()),
    user: computed(() => profileResource.value()?.data.user ?? null),
    devices: computed(() => profileResource.value()?.data.devices ?? []),
  })),
);

interface AuthState {
  loading: boolean;
  error: HttpErrorResponse | null;
}
