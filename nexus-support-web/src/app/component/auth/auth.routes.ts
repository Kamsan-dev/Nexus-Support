import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Callback } from './callback/callback';

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
        path: 'auth/callback',
        component: Callback,
      },
      {
        path: 'register',
        loadComponent: () => import('./register/register').then((c) => c.Register),
      },
      {
        path: 'verification/password',
        loadComponent: () =>
          import('./verify-password/verify-password').then((c) => c.VerifyPassword),
      },
      {
        path: 'reset/password',
        loadComponent: () => import('./reset-password/reset-password').then((c) => c.ResetPassword),
      },
      {
        path: 'verification/account',
        loadComponent: () => import('./verify-account/verify-account').then((c) => c.VerifyAccount),
      },
    ],
  },
];
