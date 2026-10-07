import { Routes } from '@angular/router';
import { Messages } from './messages';

export const MESSAGE_ROUTES: Routes = [
  {
    path: 'list',
    component: Messages,
  },

  {
    path: ':id',
    loadComponent: () => import('./message-detail/message-detail').then((c) => c.MessageDetail),
  },
];
