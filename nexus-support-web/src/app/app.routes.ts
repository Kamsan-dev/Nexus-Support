import { Routes } from '@angular/router';
import { Logout } from './component/logout/logout';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./component/guest/guest.routes').then((r) => r.GUEST_ROUTES),
  },
  {
    path: 'app',
    loadChildren: () => import('./component/features/features.routes').then((r) => r.FEATURES_ROUTES),
  },
  {
    path: 'logout',
    component: Logout,
  },
];
