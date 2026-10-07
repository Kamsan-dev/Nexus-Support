import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadChildren: () => import('./component/guest/guest.routes').then((r) => r.GUEST_ROUTES),
  },
  {
    path: 'app',
    loadChildren: () =>
      import('./component/features/features.routes').then((r) => r.FEATURES_ROUTES),
  },
];
