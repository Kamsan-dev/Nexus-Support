import { HttpErrorResponse } from '@angular/common/http';
import { computed, inject } from '@angular/core';
import { tapResponse } from '@ngrx/operators';
import { patchState, signalStore, withComputed, withMethods, withState } from '@ngrx/signals';
import { rxMethod } from '@ngrx/signals/rxjs-interop';
import { HotToastService } from '@ngxpert/hot-toast';
import { exhaustMap, pipe, switchMap, tap } from 'rxjs';
import { ConversationOverview, Message, Recipient } from '../core/model/message.model';
import { MessageService } from '../service/message.service';
import { setError, setFulfilled, setLoading, withRequestStatus } from './request-status.feature';
import { NewMessageRequest } from '../core/model/request/new-message.request';

interface state {
  conversations: ConversationOverview[];
  unreadMessagesCount: number;
  messages: Message[];
  currentConversationId: string | undefined;
  correspondentPublicId: string | undefined;
  recipients: Recipient[];
}

const initialAuthState: state = {
  conversations: [],
  unreadMessagesCount: 0,
  messages: [],
  currentConversationId: undefined,
  correspondentPublicId: undefined,
  recipients: [],
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
    loadRecipients: rxMethod<void>(
      pipe(
        tap(() => patchState(store, setLoading())),
        switchMap((request) =>
          messageService.getRecipients().pipe(
            tapResponse({
              next: (response) => {
                patchState(store, setFulfilled(), {
                  recipients: response.data,
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
    loadConversationMessages: rxMethod<string>(
      pipe(
        tap(() => patchState(store, setLoading())),
        switchMap((conversationId) =>
          messageService.getConversationMessages(conversationId).pipe(
            tapResponse({
              next: (response) => {
                patchState(store, setFulfilled(), {
                  unreadMessagesCount: store.unreadMessagesCount() - response.data.messageStatusUpdatedCount,
                  messages: response.data.messages,
                  currentConversationId: conversationId,
                  correspondentPublicId: response.data.correspondentPublicId,
                  // conversations: store
                  //   .conversations()
                  //   .map((c) => (c.conversationId === conversationId ? { ...c, lastMessageStatus: (c.lastMessageStatus = 'READ') } : c)),
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
    sendMessage: rxMethod<NewMessageRequest>(
      pipe(
        tap(() => patchState(store, setLoading())),
        exhaustMap((payload) =>
          messageService.sendMessage(payload).pipe(
            tapResponse({
              next: (response) => {
                patchState(store, setFulfilled(), {
                  conversations: [response.data, ...store.conversations().filter((c) => c.conversationId !== response.data.conversationId)],
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
