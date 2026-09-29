import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./component/auth/auth.routes').then((r) => r.AUTH_ROUTES),
  },
];
