import { Routes } from '@angular/router';
import { Home } from './home/home';
import { Callback } from './callback/callback';
import { Guest } from './guest';

export const GUEST_ROUTES: Routes = [
  {
    path: '',
    component: Guest,
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
        path: 'auth/register',
        loadComponent: () => import('./register/register').then((c) => c.Register),
      },
      {
        path: 'auth/reset/password',
        loadComponent: () => import('./reset-password/reset-password').then((c) => c.ResetPassword),
      },
      {
        path: 'verification/account',
        loadComponent: () => import('./verify-account/verify-account').then((c) => c.VerifyAccount),
      },
      {
        path: 'verification/password',
        loadComponent: () =>
          import('./verify-password/verify-password').then((c) => c.VerifyPassword),
      },
    ],
  },
];
