import { Routes } from '@angular/router';
import { Home } from './home/home';

export const AUTH_ROUTES: Routes = [
  {
    path: '',
    redirectTo: '',
    pathMatch: 'full',
  },
  {
    path: '',
    loadComponent: () => import('./auth').then((c) => c.Auth),
    children: [
      {
        path: '',
        component: Home,
      },
      {
        path: 'register',
        loadComponent: () => import('./register/register').then((c) => c.Register),
      },
      {
        path: 'verify/password',
        loadComponent: () =>
          import('./verify-password/verify-password').then((c) => c.VerifyPassword),
      },
      {
        path: 'reset-password',
        loadComponent: () => import('./reset-password/reset-password').then((c) => c.ResetPassword),
      },
      {
        path: 'verify/account',
        loadComponent: () => import('./verify-account/verify-account').then((c) => c.VerifyAccount),
      },
    ],
  },
];
