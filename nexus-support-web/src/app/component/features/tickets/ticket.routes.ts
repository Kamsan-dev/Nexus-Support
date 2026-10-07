import { Routes } from '@angular/router';
import { Tickets } from './tickets';

export const TICKET_ROUTES: Routes = [
  {
    path: 'list',
    component: Tickets,
  },

  {
    path: ':id',
    loadComponent: () => import('./ticket-detail/ticket-detail').then((c) => c.TicketDetail),
  },
];
