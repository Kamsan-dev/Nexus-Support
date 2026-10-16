import { HttpErrorResponse } from '@angular/common/http';
import { computed } from '@angular/core';
import { signalStoreFeature, withComputed, withState } from '@ngrx/signals';

type RequestStatus = 'idle' | 'loading' | 'fulfilled' | 'error';

interface RequestStatusState {
  requestStatus: RequestStatus;
  error: HttpErrorResponse | null;
}

const initialRequestStatusState: RequestStatusState = {
  requestStatus: 'idle',
  error: null,
};

export function withRequestStatus() {
  return signalStoreFeature(
    withState(initialRequestStatusState),
    withComputed((store) => ({
      loading: computed(() => store.requestStatus() === 'loading'),
      fulfilled: computed(() => store.requestStatus() === 'fulfilled'),
    })),
  );
}

export function setLoading(): RequestStatusState {
  return { requestStatus: 'loading', error: null };
}

export function setFulfilled(): RequestStatusState {
  return { requestStatus: 'fulfilled', error: null };
}

export function setError(error: HttpErrorResponse | null): RequestStatusState {
  return { requestStatus: 'error', error: error };
}
