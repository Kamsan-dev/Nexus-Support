import { HttpErrorResponse, HttpEvent, HttpHandlerFn, HttpInterceptorFn, HttpRequest } from '@angular/common/http';
import { inject } from '@angular/core';
import { HotToastService } from '@ngxpert/hot-toast';
import { catchError, finalize, Observable, shareReplay, switchMap, throwError } from 'rxjs';
import { OAuthService, TokenResponse } from '../auth/oauth.service';
import { Key } from '../enum/cache.key';
import { StorageService } from '../service/storage.service';
import { shouldNotIntercept } from './skip-url-helper';
import { Router } from '@angular/router';

let refreshToken$: Observable<TokenResponse> | null = null;

export const tokenInterceptor: HttpInterceptorFn = (req: HttpRequest<unknown>, next): Observable<HttpEvent<unknown>> => {
  const storage = inject(StorageService);
  const oauthService = inject(OAuthService);
  const toastService = inject(HotToastService);
  const router = inject(Router);
  if (shouldNotIntercept(req.url)) return next(req);
  else {
    return next(requestWithToken(req, storage.get(Key.TOKEN))).pipe(
      catchError((error: HttpErrorResponse) => {
        if (error instanceof HttpErrorResponse && error.error.code === 401 && error.error.message === 'Your session has expired.') {
          return handleRefreshToken(req, next, storage, oauthService, toastService, router);
        } else {
          return throwError(() => error);
        }
      }),
    );
  }
};

const requestWithToken = (request: HttpRequest<unknown>, accesToken: string): HttpRequest<unknown> => {
  return request.clone({ setHeaders: { Authorization: `Bearer ${accesToken}` } });
};

function handleRefreshToken(
  req: HttpRequest<unknown>,
  next: HttpHandlerFn,
  storage: StorageService,
  oauthService: OAuthService,
  toastService: HotToastService,
  router: Router,
): Observable<HttpEvent<unknown>> {
  return getRefreshToken(storage.get(Key.REFRESH_TOKEN), oauthService).pipe(
    switchMap(() => {
      return next(requestWithToken(req, storage.get(Key.TOKEN)));
    }),
    catchError((error: HttpErrorResponse) => {
      toastService.error('An error occured with the server. Please sign in.');
      router.navigate(['/logout']);
      return throwError(() => error);
    }),
  );
}

function getRefreshToken(refreshToken: string, oauthService: OAuthService): Observable<TokenResponse> {
  if (!refreshToken$) {
    refreshToken$ = oauthService.refreshToken(refreshToken).pipe(
      shareReplay({
        bufferSize: 1,
        refCount: false,
      }),
      finalize(() => {
        refreshToken$ = null;
      }),
    );
  }
  return refreshToken$;
}
