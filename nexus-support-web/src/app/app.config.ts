import { ApplicationConfig, LOCALE_ID, provideBrowserGlobalErrorListeners } from '@angular/core';
import { InMemoryScrollingFeature, provideRouter, withComponentInputBinding, withInMemoryScrolling } from '@angular/router';

import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { provideHotToastConfig } from '@ngxpert/hot-toast';
import { routes } from './app.routes';
import { tokenInterceptor } from './interceptor/token-interceptor';
import { registerLocaleData } from '@angular/common';
import localeFr from '@angular/common/locales/fr';

registerLocaleData(localeFr);

const inMemoryScrollingFeature: InMemoryScrollingFeature = withInMemoryScrolling({
  scrollPositionRestoration: 'enabled',
  anchorScrolling: 'enabled',
});

export const appConfig: ApplicationConfig = {
  providers: [
    { provide: LOCALE_ID, useValue: 'fr-FR' },
    provideBrowserGlobalErrorListeners(),
    provideRouter(routes, withComponentInputBinding(), inMemoryScrollingFeature),
    provideHotToastConfig({
      dismissible: true,
      position: 'top-right',
    }),
    provideHttpClient(withInterceptors([tokenInterceptor])),
  ],
};
