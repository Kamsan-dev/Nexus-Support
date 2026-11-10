import { HttpErrorResponse } from '@angular/common/http';
import { inject } from '@angular/core';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { HotToastService } from '@ngxpert/hot-toast';
import { pipe, switchMap, tap } from 'rxjs';
import { ConversationOverview } from '../core/model/message.model';
import { MessageService } from '../service/message.service';
import { setError, setFulfilled, setLoading, withRequestStatus } from './request-status.feature';

interface state {
  conversations: ConversationOverview[];
  unreadMessagesCount: number;
}

const initialAuthState: state = {
  conversations: [],
  unreadMessagesCount: 0,
};

export const MessageStore = signalStore(
  { providedIn: 'root' },

  withState(initialAuthState),
  withRequestStatus(),

  withMethods((store, messageService = inject(MessageService), toastService = inject(HotToastService)) => ({
    loadConversations: rxMethod<void>(
      pipe(
        tap(() => patchState(store, setLoading())),
        switchMap((request) =>
          messageService.getConversations().pipe(
            tapResponse({
              next: (response) => {
                patchState(store, setFulfilled(), {
                  conversations: response.data.conversations,
                  unreadMessagesCount: response.data.unreadMessagesCount,
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
  withComputed((store) => ({})),
);
