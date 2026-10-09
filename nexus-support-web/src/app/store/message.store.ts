import { computed, inject } from '@angular/core';
import { signalStore, withComputed, withProps, withState } from '@ngrx/signals';
import { Message } from '../core/model/message.model';
import { MessageService } from '../service/message.service';
import { rxResource } from '@angular/core/rxjs-interop';

export const MessageStore = signalStore(
  { providedIn: 'root' },

  withProps(() => {
    const messageService = inject(MessageService);

    return {
      messageResource: rxResource({
        stream: () => messageService.getAllMessages(),
      }),
    };
  }),

  withComputed(({ messageResource }) => ({
    unreadMessageCount: computed(() => getUnreadMessageCount(messageResource.value()?.data ?? [])),
  })),
);

const getUnreadMessageCount = (messages: Message[]) =>
  messages.filter((message) => message.status === 'UNREAD').length;
