import { Routes } from '@angular/router';
import { Layout } from './layout';
import { Dashboard } from './dashboard/dashboard';

export const FEATURES_ROUTES: Routes = [
  {
    path: '',
    component: Layout,
    children: [
      {
        path: 'dashboard',
        component: Dashboard,
      },
      {
        path: 'tickets',
        loadChildren: () => import('./tickets/ticket.routes').then((r) => r.TICKET_ROUTES),
      },
      {
        path: 'users',
        loadChildren: () => import('./users/user.routes').then((r) => r.USER_ROUTES),
      },
      {
        path: 'messages',
        loadChildren: () => import('./messages/message.routes').then((r) => r.MESSAGE_ROUTES),
      },
      {
        path: 'profile',
        loadComponent: () => import('./profile/profile').then((c) => c.Profile),
      },
      {
        path: 'reports',
        loadComponent: () => import('./reports/reports').then((c) => c.Reports),
      },
    ],
  },
];
