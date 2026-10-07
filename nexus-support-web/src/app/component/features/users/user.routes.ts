import { Routes } from '@angular/router';
import { Users } from './users';

export const USER_ROUTES: Routes = [
  {
    path: 'list',
    component: Users,
  },

  {
    path: ':id',
    loadComponent: () => import('./user-detail/user-detail').then((c) => c.UserDetail),
  },
];
